package cn.arry.netty.connection;

import cn.arry.Const;
import cn.arry.Log;
import cn.arry.netty.handler.CommonCmdHandler;
import cn.arry.netty.handler.ServerChannelHandler;
import cn.arry.netty.initializer.ServerChannelInitializer;
import cn.arry.netty.packet.S2SPacket;
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
    private static final int RECONNECT_INIT_TIME = 2;

    private Bootstrap bootstrap;

    /**
     * 相关操作的锁
     */
    private final ReentrantReadWriteLock connetorLock = new ReentrantReadWriteLock();

    /**
     * 是否自动连接
     */
    protected boolean isAutoConnect = true;

    /**
     * 重连次数
     */
    private int reconnectTimes = 0;

    /**
     * 是否登陆完成
     */
    private boolean loginSuccess;

    /**
     * 是否关闭入口
     */
    protected int isOpen = 1;

    /**
     * 重连线程池
     */
    private ScheduledThreadPoolExecutor service;

    /**
     * 重连延迟（单位s）
     */
    private int reconnectDelay = RECONNECT_INIT_TIME;

    /**
     * 连接地址（IP或域名）
     */
    private String address;

    /**
     * 连接端口
     */
    private int port;

    private CommonCmdHandler cmdHandler;

    /**
     * 大区id
     */
    protected int areaId;

    /**
     * 小区id
     */
    protected int regionId;

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
     * 执行连接(内部调用)
     **/
    public boolean connect() {
        try {
            connetorLock.writeLock().lock();

            if (bootstrap == null) {
                EventLoopGroup group = new NioEventLoopGroup();
                bootstrap = new Bootstrap();
                bootstrap.group(group).channel(NioSocketChannel.class);
                bootstrap.handler(new ServerChannelInitializer(new ServerChannelHandler(cmdHandler)));
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            }
            channel = bootstrap.connect(getAddress(), getPort()).sync().channel();
        } catch (Exception e) {
            Log.error("connect error address:{},port:{}", getAddress(), getPort());
            return false;
        } finally {
            connetorLock.writeLock().unlock();
        }
        return true;
    }

    /**
     * 连接
     */
    public void reconnectImpl() {
        try {
            if (isConnected()) {
                return;
            }

            if (connect()) {
                sendLogin();
                channel.attr(Const.SERVER_SESSION).set(this);
                Log.info("reconnectImpl ip:{}, port:{}. sessionId:{} ProxyClient Connect To Server Successfully!", getAddress(), getPort(), channel.id());
                return;
            }

            Log.info("ip:{}, port:{}.ProxyClient Connect To Server fail try angin!reconnectTimes:{},reconnectDelay:{}", getAddress(), getPort(), reconnectTimes, reconnectDelay);
            if (isAutoConnect) {
                reconnect();
            }
        } catch (Exception e) {
            Log.error("{}ProxyClient Disconnection Exception:", e);
        }
    }

    public void sendLogin() {
//        ServerRegisterProto.Builder builder = ServerRegisterProto.newBuilder();
//        builder.setAreaId(GlobalConfigManager.getInstance().getGlobalConfig().getAreaId());
//        builder.setRegionId(ServerConfigManager.getInstance().getServerConfig().getRegionId());
//        builder.setServerId(ServerConfigManager.getInstance().getServerConfig().getServerId());
//        sendServerMessage(CmdType.SERVERS_TO_PROXY_REGISTER, builder);
//        Log.info("Server->ProxyServer.sendRegister. ip: {}, port: {},serverId:{}", getAddress(), getPort(), builder.getServerId());
    }

    @Override
    public void disconnect() {
        loginSuccess = false;

        if (isConnected()) {
            channel.close();
            channel = null;
        }
        Log.error("disconnect begin: isconnection:{}", isAutoConnect);

        if (isAutoConnect()) {
            reconnectTimes = 0;
            reconnectDelay = RECONNECT_INIT_TIME;
            reconnect();
        }
    }

    /**
     * 连接(启动重连线程)
     **/
    public void reconnect() {
        Log.info("reconnectip: {}, port: {}.ProxyClient Connect To Server beginreconnectTimes:{},reconnectDelay:{}", getAddress(), getPort(), reconnectTimes, reconnectDelay);
        if (reconnectTimes++ >= 50) {
            if (reconnectDelay < 32)
                reconnectDelay *= 2;
        }

        service.schedule(new Runnable() {
            @Override
            public void run() {
                reconnectImpl();
            }
        }, reconnectDelay, TimeUnit.SECONDS);
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
