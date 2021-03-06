package cn.arry.netty.connection;

import cn.arry.CmdType;
import cn.arry.Log;
import cn.arry.gen.server.SCommonMsg.ServerRegisterProto;
import cn.arry.netty.handler.ServerChannelHandler;
import cn.arry.netty.handler.cmd.CommonCmdHandler;
import cn.arry.netty.initializer.ServerChannelInitializer;
import cn.arry.netty.packet.S2SPacket;
import cn.arry.type.ConstType;
import com.google.protobuf.GeneratedMessage.Builder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * s2s链接对象
 */
@Getter
@Setter
public class NettyServerConnection extends AbstractConnection {
    /**
     * 重连初始化延迟
     */
    private static final int RECONNECT_INIT_TIME = 2;

    /**
     * 重连延迟（单位s）
     */
    private int reconnectDelay = RECONNECT_INIT_TIME;

    /**
     * 是否自动连接
     */
    protected boolean isAutoConnect = true;

    /**
     * 锁
     */
    private final ReentrantReadWriteLock connetorLock = new ReentrantReadWriteLock();

    private Bootstrap bootstrap;

    /**
     * 重连次数
     */
    private int reconnectTimes = 0;

    /**
     * 是否关闭入口
     */
    protected int isOpen = 1;

    /**
     * 重连线程池
     */
    private ScheduledThreadPoolExecutor service;

    /**
     * cmd处理
     */
    private CommonCmdHandler cmdHandler;

    /**
     * 是否登陆完成
     */
    private boolean loginSuccess;

    /**
     * 连接地址（IP或域名）
     */
    private String address;

    /**
     * 连接端口
     */
    private int port;

    /**
     * 大区id
     */
    protected int areaId;

    /**
     * 小区id
     */
    protected int regionId;

    /**
     * 连接的服务器id
     */
    protected int serverId;

    /**
     * 链接服务器类型
     */
    protected int serverType;

    public NettyServerConnection(Channel channel) {
        super(channel);
        this.isAutoConnect = false;
    }

    public NettyServerConnection(String address, int port, CommonCmdHandler cmdHandler) {
        this.address = address;
        this.port = port;
        this.isAutoConnect = true;
        this.isOpen = 1;
        this.cmdHandler = cmdHandler;
    }

    public void setAutoConnect(boolean autoConnect) {
        isAutoConnect = autoConnect;
    }

    /**
     * 执行连接（as a client）
     **/
    public boolean connect() {
        connetorLock.writeLock().lock();
        try {
            if (bootstrap == null) {
                EventLoopGroup group = new NioEventLoopGroup();
                bootstrap = new Bootstrap();
                bootstrap.group(group).channel(NioSocketChannel.class);
                bootstrap.handler(new ServerChannelInitializer(new ServerChannelHandler(cmdHandler)));
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            }

            channel = bootstrap.connect(getAddress(), getPort()).sync().channel();
        } catch (Exception e) {
            Log.error("NettyServerConnection->connect error, address:{}, port:{}", getAddress(), getPort());
            return false;
        } finally {
            connetorLock.writeLock().unlock();
        }

        return true;
    }

    /**
     * 掉线
     */
    @Override
    public void disconnect() {
        loginSuccess = false;
        if (isConnected()) {
            channel.close();
            channel = null;
        }

        Log.error("NettyServerConnection->disconnect, isAutoConnect:{}", isAutoConnect);
        if (isAutoConnect()) {
            reconnectTimes = 0;
            reconnectDelay = RECONNECT_INIT_TIME;
            reconnect();
        }
    }

    /**
     * 重连
     **/
    public void reconnect() {
        Log.info("NettyServerConnection->reconnect, ip:{}, port:{}, reconnectTimes:{}, reconnectDelay:{}",
                getAddress(), getPort(), reconnectTimes, reconnectDelay);
        if (reconnectTimes++ >= 50) {
            if (reconnectDelay < 32) {
                reconnectDelay *= 2;
            }
        }

        service.schedule(this::reconnectImpl, reconnectDelay, TimeUnit.SECONDS);
    }

    /**
     * 连接
     */
    private void reconnectImpl() {
        try {
            if (isConnected()) {
                return;
            }

            if (connect()) {
                sendLogin();
                channel.attr(ConstType.SERVER_SESSION).set(this);
                Log.info("NettyServerConnection->reconnectImpl, reconnectImpl done! ip:{}, port:{}, sessionId:{}",
                        getAddress(), getPort(), channel.id());
                return;
            }

            Log.info("NettyServerConnection->reconnectImpl, try again! ip:{}, port:{}, reconnectTimes:{}, reconnectDelay:{}",
                    getAddress(), getPort(), reconnectTimes, reconnectDelay);
            if (isAutoConnect) {
                reconnect();
            }
        } catch (Exception e) {
            Log.error("NettyServerConnection->reconnectImpl error", e);
        }
    }

    /**
     * 发送注册
     */
    private void sendLogin() {
        ServerRegisterProto.Builder builder = ServerRegisterProto.newBuilder();
        builder.setServerId(ConstType.TEMP_GAME_SERVER_ID);
        sendServerMessage(CmdType.S2S_SERVER_LOGIN, builder);
        Log.info("NettyServerConnection->sendLogin, Server to ProxyServer sendRegister, ip:{}, port:{}, serverId:{}",
                getAddress(), getPort(), builder.getServerId());
    }

    @Override
    public boolean isConnected() {
        if (channel != null && channel.isActive()) {
            return true;
        }

        return false;
    }

    /**
     * server包
     */
    public void sendServerMessage(short code, Builder<?> builder) {
        S2SPacket packet = new S2SPacket(code);
        if (builder != null)
            packet.setBuilder(builder);

        send(packet);
    }

    /**
     * server包->发送给特定玩家
     */
    public void sendServerMessage(short code, Builder<?> builder, long userId) {
        S2SPacket packet = new S2SPacket(code);
        packet.setDestUserID(userId);
        if (builder != null)
            packet.setBuilder(builder);

        send(packet);
    }

    /**
     * server包->发给特定服务器
     */
    public void sendServerMessage(short code, Builder<?> builder, int destServerId) {
        S2SPacket packet = new S2SPacket(code);
        packet.setDestServerID(destServerId);
        if (builder != null)
            packet.setBuilder(builder);

        send(packet);
    }
}
