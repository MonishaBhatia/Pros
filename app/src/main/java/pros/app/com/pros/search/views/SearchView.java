package pros.app.com.pros.search.views;

import java.util.ArrayList;

import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;

public interface SearchView {
    void updateTopPros(ArrayList<AthleteModel> topProsList);

    void updateTopPosts(ArrayList<PostModel> topPostsList);
}
