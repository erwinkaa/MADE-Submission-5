package id.erwinka.madesubmission4.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v14.preference.SwitchPreference
import android.support.v7.preference.PreferenceFragmentCompat
import id.erwinka.madesubmission4.R

class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var dailySwitch: SwitchPreference
    private lateinit var releaseTodaySwitch: SwitchPreference
    private var keyDaily = ""
    private var keyReleaseToday = ""

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.setting)
        init()
        setSettings()
    }

    private fun init() {
        dailySwitch = findPreference(resources.getString(R.string.key_daily)) as SwitchPreference
        releaseTodaySwitch = findPreference(resources.getString(R.string.key_release_today)) as SwitchPreference

        keyDaily = resources.getString(R.string.key_daily)
        keyReleaseToday = resources.getString(R.string.key_release_today)
    }

    private fun setSettings() {
        val sh = preferenceManager.sharedPreferences
        dailySwitch.isChecked = sh.getBoolean(keyDaily, false)
        releaseTodaySwitch.isChecked = sh.getBoolean(keyReleaseToday, false)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when (key) {
            resources.getString(R.string.key_daily) -> {
                dailySwitch.isChecked = sharedPreferences.getBoolean(keyDaily, false)
                setReminder(keyDaily)
            }
            resources.getString(R.string.key_release_today) -> {
                releaseTodaySwitch.isChecked = sharedPreferences.getBoolean(keyReleaseToday, false)
                setReminder(keyReleaseToday)
            }
        }
    }

    private fun setReminder(key: String) {
        val isActive = preferenceManager.sharedPreferences.getBoolean(key, false)
        val alarmReceiver = AlarmReceiver()
        if (isActive) {
            if (key == keyDaily) {
                alarmReceiver.setRepeatingAlarm(activity, AlarmReceiver.DAILY)
            } else if (key == keyReleaseToday) {
                alarmReceiver.setRepeatingAlarm(activity, AlarmReceiver.RELEASE_DATE)
            }
        } else {
            if (key == keyDaily) {
                alarmReceiver.cancelRepeatingAlarm(activity, AlarmReceiver.DAILY)
            } else if (key == keyReleaseToday) {
                alarmReceiver.cancelRepeatingAlarm(activity, AlarmReceiver.RELEASE_DATE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

}