package cn.arry;

/**
 * 协议号
 * 1 - 10000 c2s
 * 10001 - 32767 s2s
 *
 * @author Arry
 * @version 2019/12/10
 */
public interface CmdType {
    // c2s begin
    short C2S_SAY_HELLO = 1;
    // c2s end

    // s2s begin
    short S2S_SERVER_LOGIN = 10001;
    // s2s end
}
