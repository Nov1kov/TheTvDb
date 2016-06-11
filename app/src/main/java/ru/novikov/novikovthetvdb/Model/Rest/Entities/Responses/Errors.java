package ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses;

import java.util.ArrayList;
import java.util.List;

public class Errors {

    public List<String> invalidFilters = new ArrayList<String>();
    public String invalidLanguage;
    public List<String> invalidQueryParams = new ArrayList<String>();

}
