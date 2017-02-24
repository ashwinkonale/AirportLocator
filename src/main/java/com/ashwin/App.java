package com.ashwin;

import com.ashwin.flink.AirportLocateMapFunction;
import com.ashwin.utils.AirportDataLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

import java.io.File;
import java.io.IOException;

public class App {
    private AppConfig appConfig;

    public App(String configurationFile) throws IOException {
        loadConfiguration(configurationFile);
    }

    private void loadConfiguration(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        appConfig = mapper.readValue(new File(filename), AppConfig.class);
    }

    public static void main(String[] args) throws Exception {
        String configurationFile = args[0];
        App app = new App(configurationFile);

        AppConfig appConfig = app.appConfig;
        new AirportDataLoader(appConfig).load();

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<Tuple3<String, Double, Double>> types = env.readCsvFile(appConfig.usersDataFilePath)
                .ignoreFirstLine().types(String.class, Double.class, Double.class);

        DataSet<Tuple2<String, String>> set = types.map(new AirportLocateMapFunction(appConfig.redisConfig));

        // Can increase parallelism for distributed writing.
        set.writeAsCsv("file://" + appConfig.outputFilePath).setParallelism(1);
        env.execute();
    }
}
