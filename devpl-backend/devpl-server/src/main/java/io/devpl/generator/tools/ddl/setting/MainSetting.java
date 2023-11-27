package io.devpl.generator.tools.ddl.setting;

public class MainSetting {

    private static final MainSetting mainSetting = new MainSetting();
    public MySettingProperties myProperties = new MySettingProperties();

    public static MainSetting getInstance() {
        return mainSetting;
    }

    public MySettingProperties getState() {
        return myProperties;
    }
}
