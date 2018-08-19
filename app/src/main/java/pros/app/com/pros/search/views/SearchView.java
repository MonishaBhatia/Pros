package pros.app.com.pros.search.views;

import java.util.ArrayList;

import pros.app.com.pros.home.model.AthleteModel;

public interface SearchView {
    void updateTopPros(ArrayList<AthleteModel> topProsList);

    void updateTopPosts();
}
