package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Model.Settings.AppSettings;
import org.example.newtopsquadproject.Repository.Settings.AppSettingsRepo;
import org.springframework.stereotype.Service;

@Service
public class AppSettingsService {

    private final AppSettingsRepo appSettingsRepo;

    public AppSettingsService(AppSettingsRepo appSettingsRepo) {
        this.appSettingsRepo = appSettingsRepo;
    }

    public void createAppSetting(String setting, String state){
        AppSettings appSetting = new AppSettings(setting, state);
        appSettingsRepo.save(appSetting);
    }

    public void updateAppSetting(String setting, String newState){
        AppSettings appSetting = appSettingsRepo.findById(setting).orElseThrow(() -> new ResourceNotFoundException("App setting not found with name: " + setting));

        if(appSetting.getState().equalsIgnoreCase(newState)){
            return;
        }

        appSetting.setState(newState);
        appSetting.setTime();
        appSettingsRepo.save(appSetting);
    }

    public String getSettingState(String setting){
        AppSettings appSetting = appSettingsRepo.findById(setting).orElseThrow(() -> new ResourceNotFoundException("App setting not found with name: " + setting));
        return appSetting.getState();
    }
}
