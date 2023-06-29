package io.devpl.codegen.ddl.setting;

public class MainSetting {

    public MySettingProperties myProperties = new MySettingProperties();

    private static final MainSetting mainSetting = new MainSetting();

    public static MainSetting getInstance() {
        return mainSetting;
    }

    public MySettingProperties getState() {
        return myProperties;
    }
}
