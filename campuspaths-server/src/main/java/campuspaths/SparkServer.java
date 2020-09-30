package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Map;


public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        CampusMap campusMap = new CampusMap();
        Spark.get("/list-building", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Map<String, String> shortToLong = campusMap.buildingNames();
                Gson gson = new Gson();
                return gson.toJson(shortToLong);
            }
        });

        Spark.get("/find-path", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String startBuilding = request.queryParams("start");
                String endBuilding = request.queryParams("end");
                if (startBuilding == null || endBuilding == null) {
                    Spark.halt(400, "must have a starting point and a destination" +
                            " point");
                }
                if (!campusMap.shortNameExists(startBuilding) ||
                        !campusMap.shortNameExists(endBuilding)) {
                    if (!campusMap.shortNameExists(startBuilding)) {
                        Spark.halt(400, "the name of the starting point does not exist.");
                    }
                    if (!campusMap.shortNameExists(endBuilding)) {
                        Spark.halt(400, "the name of the destination point does not exist");
                    }
                }
                Gson gson = new Gson();
                return gson.toJson(campusMap.findShortestPath(startBuilding, endBuilding));
            }
        });
    }
}