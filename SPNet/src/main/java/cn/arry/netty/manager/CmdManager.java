package cn.arry.netty.manager;

import cn.arry.Log;
import cn.arry.netty.cmd.ICode;
import cn.arry.netty.cmd.ICommand;
import cn.arry.utils.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdManager {
    private static Map<Short, ICommand> cmdCache = new HashMap<>();

    public static boolean init(String packageName, String extraPackage) {
        return loadCmd(packageName) && loadCmd(extraPackage);
    }

    private static boolean loadCmd(String packName) {
        try {
            if (packName == null) {
                return true;
            }

            List<Class<?>> cmds = ClassUtil.getClasses(packName);
            if (cmds == null || cmds.isEmpty()) {
                Log.info("CmdCache->loadCmd, no cmd");
                return true;
            }

            for (Class<?> clazz : cmds) {
                ICode cmd = clazz.getAnnotation(ICode.class);
                if (cmd != null) {
                    Short code = cmd.code();
                    if (cmdCache.get(code) != null) {
                        String name = cmdCache.get(code).getClass().getName();
                        Log.error("CmdCache->loadCmd, code:{} is repeated, name:{}, newName:{}", code, name,
                                clazz.getName());
                    }

                    cmdCache.put(code, (ICommand) clazz.newInstance());
                }
            }

            Log.info("CmdCache->loadCmd, loaded command from [" + packName + "], count:" + cmdCache.size());
        } catch (Exception e) {
            Log.error("CmdCache->loadCmd error", e);
        }

        return true;
    }

    public static ICommand getCommand(short code) {
        return cmdCache.get(code);
    }
}
