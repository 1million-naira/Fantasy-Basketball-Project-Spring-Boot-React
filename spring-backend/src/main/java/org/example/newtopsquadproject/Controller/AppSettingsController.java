package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Service.AppSettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/settings")
public class AppSettingsController {

    private final AppSettingsService appSettingsService;

    public AppSettingsController(AppSettingsService appSettingsService) {
        this.appSettingsService = appSettingsService;
    }

    @GetMapping("/transferMarket")
    public ResponseEntity<Boolean> transferMarketStatus(){
        boolean open = false;
        String state = appSettingsService.getSettingState("transferMarketStatus");
        if(state.equalsIgnoreCase("open")){
            open = true;
        }
        return new ResponseEntity<>(open, HttpStatus.OK);
    }
}
