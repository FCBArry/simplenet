package cn.arry.netty.manager;

import cn.arry.ClassUtil;
import cn.arry.Log;
import cn.arry.netty.cmd.ICode;
import cn.arry.netty.cmd.ICommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdCacheManager {
    //指令集合，short指令足够用
    private static Map<Short, ICommand> cmdCache = new HashMap<>();

    public static boolean init(String packageName, String extraPackage) {
        return initCmdCache(packageName) && initCmdCache(extraPackage);
    }

    /**
     * 初始化指令集合
     *
     * @param packName
     * @return
     */
    private static boolean initCmdCache(String packName) {
        return loadCmd(packName);
    }

    public static boolean loadCmd(String packName) {
        try {
            if (packName == null) {
                return true;
            }
            List<Class<?>> cmds = ClassUtil.getClasses(packName);
            for (Class<?> clazz : cmds) {
                ICode cmd = clazz.getAnnotation(ICode.class);
                if (cmd != null) {
                    Short code = cmd.code();
                    if (cmdCache.get(code) != null) {
                        String name = cmdCache.get(code).getClass().getName();
                        Log.warn("cmd code overwrited, code : " + code + " exist :" + name
                                + ", new : " + clazz.getName());
                    }
                    cmdCache.put(code, (ICommand) clazz.newInstance());
                }
            }
            Log.info("loaded command from [" + packName + "], count: " + cmdCache.size());
        } catch (Exception e) {
            Log.error("CmdCache->loadCmd error", e);
        }
        return true;
    }

    public static ICommand getCommand(short code) {
        return cmdCache.get(code);
    }


}
