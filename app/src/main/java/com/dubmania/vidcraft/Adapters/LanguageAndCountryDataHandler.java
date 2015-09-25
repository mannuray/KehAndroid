package com.dubmania.vidcraft.Adapters;

import java.util.ArrayList;

/**
 * Created by rat on 9/24/2015.
 */
public class LanguageAndCountryDataHandler {

    private ArrayList<Language> mLanguages;

    public LanguageAndCountryDataHandler(ArrayList<Language> mLanguages) {
        this.mLanguages = mLanguages;
    }

    public String[] getLanguages() {
        String[] languages = new String[mLanguages.size()];
        for(int i = 0; i < mLanguages.size(); i++) {
            languages[i] = mLanguages.get(i).getLanguage();
        }
        return languages;
    }

    public ArrayList<Language> getLanguagesArray() {
        return mLanguages;
    }

    public String[] getCountries(int pos) {
        return mLanguages.get(pos).getCountries();
    }

    public Language getLanguage(int position) {
        return mLanguages.get(position);
    }

    public int getLanguageSize() {
        return mLanguages.size();
    }

    public int getCountriesSize(int pos) {
        return mLanguages.get(pos).getCountriesSize();
    }

    public static class Language {
        private Long id;
        private String mLanguage;
        private ArrayList<Country> mCountries;

        public Language(Long id, String mLanguage) {
            this.id = id;
            this.mLanguage = mLanguage;
            mCountries = new ArrayList<>();
        }

        public Long getId() {
            return id;
        }

        public String getLanguage() {
            return mLanguage;
        }

        public Country getCountry(int position) {
            return mCountries.get(position);
        }

        public void addCountry(Country country) {
            mCountries.add(country);
        }

        public String[] getCountries() {
            String[] countries = new String[mCountries.size()];
            for(int i = 0; i < mCountries.size(); i++) {
                countries[i] = mCountries.get(i).getCountry();
            }
            return countries;
        }

        public int getCountriesSize() {
            return mCountries.size();
        }
    }

    public static class Country {
        private Long id;
        private String mCountry;

        public Country(Long id, String mCountry) {
            this.id = id;
            this.mCountry = mCountry;
        }

        public Long getId() {
            return id;
        }

        public String getCountry() {
            return mCountry;
        }
    }
}
