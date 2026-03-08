package org.example.newtopsquadproject.Repository.Settings;

import org.example.newtopsquadproject.Model.Settings.AppSettings;
import org.springframework.data.repository.CrudRepository;

public interface AppSettingsRepo extends CrudRepository<AppSettings, String> {
}
