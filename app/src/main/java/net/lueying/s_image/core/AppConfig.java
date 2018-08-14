package net.lueying.s_image.core;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * app配置文件
 */
public class AppConfig {

    public final static String CONF_APP_UUID = "conf_app_uuid";

    private final static String CONFIG_DIR = "config";
    private Context mContext;
    private static AppConfig instance;

    // 日志文件的路径
    public final static String DEFAULT_SAVE_LOG_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "LveYing"
            + File.separator + "log" + File.separator;

    // WebView缓存的路径
    public final static String WEBVIEW_CACHE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "LveYing"
            + File.separator + "webview" + File.separator;

    //存放视频文件的路径
    public final static String DEFAULT_SAVE_VIDEO_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "LveYing"
            + File.separator + "video" + File.separator;

    public static AppConfig getInstance(Context context) {
        if (instance == null) {
            instance = new AppConfig();
            instance.mContext = context;
        }
        return instance;
    }

    public String get(String key) {
        String val = null;
        Properties props = get();
        if (props != null && props.containsKey(key)) {
            val = props.getProperty(key);
        }
        return val;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(CONFIG_DIR, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + CONFIG_DIR);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(CONFIG_DIR, Context.MODE_PRIVATE);
            File conf = new File(dirConf, CONFIG_DIR);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}
