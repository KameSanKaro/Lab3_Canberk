package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private ArrayList<ArrayList<ArrayList<String>>> countryLanguages = new ArrayList<>();
    private ArrayList<String> countries = new ArrayList<>();
    private JSONArray countriesJSON;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);

            // TODO Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.

            for (Object countryData: jsonArray) {
                JSONObject jsonObject = (JSONObject) countryData;
                ArrayList<String> countryCode = new ArrayList<>();

                countryCode.add(jsonObject.getString("alpha3"));
                String keyStr0 = jsonObject.keySet().toString();
                String keyStr = keyStr0.substring(1, keyStr0.length() - 2);
                String[] keys = keyStr.split(", ");
                ArrayList<String> languageCodes = new ArrayList<>(Arrays.asList(keys));
                languageCodes.remove("alpha2");
                languageCodes.remove("alpha3");
                languageCodes.remove("id");

                ArrayList<ArrayList<String>> countryArray = new ArrayList<>();

                countryArray.add(countryCode);
                countries.add(countryCode.get(0));

                countryArray.add(languageCodes);
                countryLanguages.add(countryArray);
                countriesJSON = jsonArray;
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        for (ArrayList<ArrayList<String>> countryArray: countryLanguages) {
            if (country.equals(countryArray.get(0).get(0))) {
                return countryArray.get(1);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed

        try {
            for (Object subarray: countriesJSON) {
                JSONObject jsonObject = (JSONObject) subarray;
                if (jsonObject.getString("alpha3").equals(country)) {
                    return jsonObject.getString(language);
                }
            }
        }
        catch (JSONException ex) {
            System.out.println("Problem!");
        }
        return "Country not found";
    }
}
