package com.lechucksoftware.proxy.proxysettings;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.lechucksoftware.proxy.proxysettings.constants.AndroidMarket;
import com.lechucksoftware.proxy.proxysettings.constants.Intents;
import com.lechucksoftware.proxy.proxysettings.db.DataSource;
import com.lechucksoftware.proxy.proxysettings.utils.EventReportingUtils;
import com.lechucksoftware.proxy.proxysettings.utils.Utils;
import com.shouldit.proxy.lib.APL;
import com.shouldit.proxy.lib.log.LogWrapper;


public class ApplicationGlobals extends Application
{
    private static final String TAG = ApplicationGlobals.class.getSimpleName();

    private static ApplicationGlobals mInstance;
    private ProxyManager proxyManager;
    private DataSource dbManager;
    public AndroidMarket activeMarket;
    private CacheManager cacheManager;
    public Boolean demoMode;
    public Boolean wifiActionEnabled;
    private LogWrapper logger;

    @Override
    public void onCreate()
    {
        super.onCreate();

//        // SLF4J
//        Logger LOG = LoggerFactory.getLogger(ApplicationGlobals.class);
//        LOG.info("hello world");
        getLogger().startTrace(TAG, "STARTUP", Log.ERROR);

        mInstance = this;

        proxyManager = new ProxyManager(ApplicationGlobals.this);
        dbManager = new DataSource(ApplicationGlobals.this);
        cacheManager = new CacheManager(ApplicationGlobals.this);

        activeMarket = Utils.getInstallerMarket(ApplicationGlobals.this);

        demoMode = false;
        wifiActionEnabled = true;

        // READ configuration file
//        readAppConfigurationFile();

        // SETUP Libraries
        EventReportingUtils.setup(ApplicationGlobals.this);
        APL.setup(ApplicationGlobals.this, getLogger().getLogLevel(), EventReportingUtils.getInstance());

        getLogger().d(TAG, "Calling broadcast intent " + Intents.PROXY_SETTINGS_STARTED);
        sendBroadcast(new Intent(Intents.PROXY_SETTINGS_STARTED));
    }

//    public void readAppConfigurationFile()
//    {
//        LogWrapper.startTrace(TAG,"readAppConfigurationFile",Log.INFO);
//
//        try
//        {
//            AssetManager am = getAssets();
//            if (am != null)
//            {
//                InputStream inputStream = am.open("configuration.json");
//                if (inputStream != null)
//                {
//                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder builder = new StringBuilder();
//                    String line = "";
//
//                    while ((line = br.readLine()) != null) {
//                        builder.append(line);
//                    }
//
//                    String configuration = builder.toString();
//
//                    JSONObject jsonObject = new JSONObject(configuration);
//
//                    // If you want to use BugSense for your fork, register with
//                    // them and place your API key in /assets/bugsense.txt
//                    // (This prevents me receiving reports of crashes from forked
//                    // versions which is somewhat confusing!)
//
//                    if (jsonObject.has("bugsense"))
//                    {
//                        JSONObject bugsense = jsonObject.getJSONObject("bugsense");
//                        if (bugsense.has("release-key"))
//                        {
//                            BugsenseReleaseKey = bugsense.getString("release-key");
//                        }
//
//                        if (bugsense.has("development-key"))
//                        {
//                            BugsenseDevelopmentKey = bugsense.getString("development-key");
//                        }
//                    }
//                }
//            }
//        }
//        catch (IOException e)
//        {
//            LogWrapper.e(TAG, "No configuration file found");
//            return;
//        }
//        catch (Exception e)
//        {
//            LogWrapper.e(TAG, "Generic exception during read of configuration file: " + e.toString());
//            return;
//        }
//
//        LogWrapper.stopTrace(TAG, "readAppConfigurationFile", Log.INFO);
//    }

    public static LogWrapper getLogger()
    {
        if (getInstance().logger == null)
        {
            getInstance().logger = new LogWrapper(Log.INFO);
        }

        return getInstance().logger;
    }

    public static ApplicationGlobals getInstance()
    {
        if (mInstance == null)
        {
            EventReportingUtils.sendException(new Exception("Cannot find valid instance of ApplicationGlobals, trying to instanciate a new one"));
            mInstance = new ApplicationGlobals();
        }

        return mInstance;
    }

    public static ProxyManager getProxyManager()
    {
        if (getInstance().proxyManager == null)
        {
            EventReportingUtils.sendException(new Exception("Cannot find valid instance of ProxyManager, trying to instanciate a new one"));
            getInstance().proxyManager = new ProxyManager(getInstance());
        }

        return getInstance().proxyManager;
    }

    public static DataSource getDBManager()
    {
        if (getInstance().dbManager == null)
        {
            EventReportingUtils.sendException(new Exception("Cannot find valid instance of DataSource, trying to instanciate a new one"));
            getInstance().dbManager = new DataSource(getInstance());
        }

        return getInstance().dbManager;
    }

    public static CacheManager getCacheManager()
    {
        if (getInstance().cacheManager == null)
        {
            EventReportingUtils.sendException(new Exception("Cannot find valid instance of CacheManager, trying to instanciate a new one"));
            getInstance().cacheManager = new CacheManager(getInstance());
        }

        return getInstance().cacheManager;
    }
}
