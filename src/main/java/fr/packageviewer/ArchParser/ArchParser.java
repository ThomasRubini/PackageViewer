package fr.packageviewer.ArchParser;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArchParser {

    public String getPackageFromAPI(String packageName){
        String jsonStr = """
             {
                 "version": 2,
                 "limit": 250,
                 "valid": true,
                 "results": [
                     {
                         "pkgname": "bash",
                         "pkgbase": "bash",
                         "repo": "core",
                         "arch": "x86_64",
                         "pkgver": "5.1.016",
                         "pkgrel": "1",
                         "epoch": 0,
                         "pkgdesc": "The GNU Bourne Again shell",
                         "url": "https://www.gnu.org/software/bash/bash.html",
                         "filename": "bash-5.1.016-1-x86_64.pkg.tar.zst",
                         "compressed_size": 1707705,
                         "installed_size": 8592907,
                         "build_date": "2022-01-08T18:31:11Z",
                         "last_update": "2022-01-09T19:38:32.491Z",
                         "flag_date": "2022-09-26T16:50:03.401Z",
                         "maintainers": [
                             "felixonmars",
                             "anthraxx",
                             "grazzolini"
                         ],
                         "packager": "felixonmars",
                         "groups": [],
                         "licenses": [
                             "GPL"
                         ],
                         "conflicts": [],
                         "provides": [
                             "sh"
                         ],
                         "replaces": [],
                         "depends": [
                             "glibc",
                             "libreadline.so=8-64",
                             "ncurses",
                             "readline"
                         ],
                         "optdepends": [
                             "bash-completion: for tab completion"
                         ],
                         "makedepends": [],
                         "checkdepends": []
                     }
                 ],
                 "num_pages": 1,
                 "page": 1
             }""";
        return jsonStr;
    }

    /**
     * Will parse the given json and store data in a Package
     * @param jsonResponse the given json string
     * @param depth depth to search dependencies
     * @return new Package
     */

    public Package getPackageTree(String packageName, int depth){
        String name, version, repo, description;
        JSONArray dependencies;
        List<Package> deps;

        // parse the json
        JSONObject json = new JSONObject(getPackageFromAPI(packageName));
        //
        JSONObject resultJson = json.getJSONArray("results").getJSONObject(0);

        // get infos except dependencies
        name = resultJson.getString("pkgname");
        version = resultJson.getString("pkgver");
        repo = resultJson.getString("repo");
        description = resultJson.getString("pkgdesc");

        System.out.println(name);
        return null;
    }
}
