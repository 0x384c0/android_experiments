package com.desu.experiments.view.activity.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.desu.experiments.R;
import com.desu.experiments.model.JSONResponse.Features;
import com.desu.experiments.model.JSONResponse.JSONResponse;
import com.desu.experiments.model.JSONResponse.Point;
import com.desu.experiments.model.JSONResponseRealm.Email;
import com.desu.experiments.model.JSONResponseRealm.RealmDatabase;
import com.desu.experiments.retrofit.api.RetrofitApis;
import com.desu.experiments.util.RxUtils;
import com.desu.experiments.view.widget.logView.LogView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func6;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RxRetrofit extends AppCompatActivity {

    String KAIDA_URL = "http://kayakta.osmonov.com/api/";
    String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
    String WEATHER_API_KEY = "56b9797a189525262609bdc2017e0a39";

    CompositeSubscription _subscriptions = new CompositeSubscription();
    @Bind(R.id.log_list)
    LogView logView;
    @Bind(R.id.rx_retrofit_progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retrofit);
        ButterKnife.bind(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }

    public void getPoints(View v) {
        progressBar.setVisibility(View.VISIBLE);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KAIDA_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        RetrofitApis retrofitApis = retrofit.create(RetrofitApis.class);


        _subscriptions.add(
                retrofitApis.getPointsRX()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(jsonResponseObserver())
        );
    }

    private Observer<JSONResponse> jsonResponseObserver() {
        return new Observer<JSONResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("-------------- ERROR ", e.toString());
            }

            @Override
            public void onNext(JSONResponse jsonResponse) {

                List<Point> points = jsonResponse.points;


                ActiveAndroid.beginTransaction();
                try {

                    List<JSONResponse> jsonResponses = new Select().from(JSONResponse.class).execute();

                    for (JSONResponse jsonResponseDel : jsonResponses) jsonResponseDel.delete();
                    ActiveAndroid.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0");
                    Log.e("LIST COUNT IS", String.valueOf(points.size()));

                    jsonResponse.save();
                    for (Point point : points) {
                        point.jsonResponse = jsonResponse;
                        point.save();

                        point.coordinate.point = point;
                        point.coordinate.save();

                        point.vendor.point = point;
                        point.vendor.save();

                        point.vendor.network.vendor = point.vendor;
                        point.vendor.network.save();

                        List<Features> featuresList = point.features;

                        for (Features features : featuresList) {
                            features.point = point;
                            features.save();
                        }
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }

                final ArrayList<String> lst = new ArrayList<>();
                for (int i = 0; i < points.size(); i++) {
                    lst.add(points.get(i).title + " | - " + points.get(i).getVendor().getNetwork().title);
                    //System.out.println("title is \t" + points.get(i).title);
                    //System.out.println("id is \t" + points.get(i).id);
                }
                logView.log(lst);
                runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
                test(50);
            }
        };
    }

    void test(int index) {


        JSONResponse db = JSONResponse.load(JSONResponse.class, 1);

        List<Point> pointList = db.getPoints();


        Point point = pointList.get(index);

        System.out.println("point.id                             \t" + point.id);
        System.out.println("point.title                          \t" + point.title);
        System.out.println("point.phone                          \t" + point.phone);
        System.out.println("point.address                        \t" + point.address);
        System.out.println("point.getCoordinate().lat            \t" + point.getCoordinate().lat);
        System.out.println("point.getVendor().title              \t" + point.getVendor().title);
        System.out.println("point.getVendor().getNetwork().title \t" + point.getVendor().getNetwork().title);

        List<Features> featuresList = point.getFeaturesList();

        for (Features features : featuresList) {
            System.out.println("point.getFeaturesList().get(i).title \t" + features.title);
        }

    }


    public void getWeather(View v) {
        progressBar.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(chain -> {
            Request original = chain.request();
            HttpUrl url = original.httpUrl().newBuilder()
                    .addQueryParameter("AppId", WEATHER_API_KEY)
                    .build();

            Request request = original.newBuilder()
                    .url(url)
                    /*.header("AppId", WEATHER_API_KEY)
                    .method(original.method(), original.body())*/
                    .build();
            return chain.proceed(request);
        });
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        RetrofitApis retrofitApis = retrofit.create(RetrofitApis.class);

        Func6<Response<ResponseBody>, Response<ResponseBody>, Response<ResponseBody>, Response<ResponseBody>, Response<ResponseBody>, Response<ResponseBody>, ArrayList<String>>
                parseJson = (response1, response2, response3, response4, response5, response6) -> {
            ArrayList<String> lst = new ArrayList<>();
            try {
                JSONObject jsonObject;
                lst.add(response1.body().string());
                jsonObject = new JSONObject(response2.body().string());
                lst.add(jsonObject.getString("name") + " - " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                jsonObject = new JSONObject(response3.body().string());
                lst.add(jsonObject.getString("name") + " - " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                jsonObject = new JSONObject(response4.body().string());
                lst.add(jsonObject.getString("name") + " - " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                jsonObject = new JSONObject(response5.body().string());
                lst.add(jsonObject.getString("name") + " - " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                jsonObject = new JSONObject(response6.body().string());
                lst.add(jsonObject.getString("name") + " - " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lst;
        };

        Observable.zip(
                retrofitApis.get("Bishkek"),
                retrofitApis.get("London"),
                retrofitApis.get("New York, US"),
                retrofitApis.get("Moscow"),
                retrofitApis.get("Paris, FR"),
                retrofitApis.get("Tokyo"),
                parseJson
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArrayList<String> lst) {
                        logView.log(lst);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }


    public void writeRealmDatabase(View v) {
        progressBar.setVisibility(View.VISIBLE);
        Realm realm = Realm.getInstance(this);

        RealmDatabase realmDatabase = new RealmDatabase();
        realmDatabase.setAge(42);
        realmDatabase.setName("John");
        realmDatabase.setSessionId(1);

        //Many-to-One
        Email email1 = new Email();
        email1.setAddress("123@example.com");
        email1.setActive(true);
        realmDatabase.setEmail(email1);


        //Many-to-Many
        realmDatabase.setEmails(new RealmList<>());
        Email email2 = new Email();
        email2.setAddress("john@example.com");
        email2.setActive(true);
        realmDatabase.getEmails().add(email2);

        Email email3 = new Email();
        email3.setAddress("jd@example.com");
        email3.setActive(false);
        realmDatabase.getEmails().add(email3);

        //write
        realm.beginTransaction();
        realm.clear(RealmDatabase.class);
        realm.copyToRealmOrUpdate(realmDatabase);
        realm.commitTransaction();
        System.out.println("write");

        //read
        RealmResults<RealmDatabase> result2 = realm.where(RealmDatabase.class)
                .findAll();
        ArrayList<String> lst = new ArrayList<>();
        lst.add("name        " + result2.first().getName());
        lst.add("email       " + result2.first().getEmail().getAddress());
        lst.add("emails 1    " + result2.first().getEmails().get(1).getAddress());
        logView.log(lst);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
