package com.desu.experiments.view.activity.others;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.internal.util.Predicate;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.desu.experiments.R;
import com.desu.experiments.view.widget.logView.LogView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LambdaExpressions extends AppCompatActivity {

    @Bind(R.id.log_list)
    LogView logView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lambda_expressions);
        ButterKnife.bind(this);
    }

    private void log(String logMsg) {
        logView.log(logMsg);
    }



    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);


    public void predicatesClick(View v) {
        log("predicatesClick-------");
        log("n -> true         " + sumAll(numbers, n -> true));
        log("n -> n % 2 == 0   " + sumAll(numbers, n -> n % 2 == 0));
        log("n -> n > 3        " + sumAll(numbers, n -> n > 3));


    }

    public int sumAll(List<Integer> numbers, Predicate<Integer> p) {
        int total = 0;
        for (int number : numbers) {
            if (p.apply(number)) {
                total += number;
            }
        }
        return total;
    }


    public void LazyStreamClick(View view) {
        log("LazyStreamClick-------");
        List<Integer> tmp = Stream.of(numbers)
                .filter(Lazy::isEven)
                .map(Lazy::doubleIt)
                .filter(Lazy::isGreaterThan5)
                .collect(Collectors.toList());
        log(tmp.toString());
    }

    static class Lazy {
        static boolean isEven(int number) {
            return number % 2 == 0;
        }

        static int doubleIt(int number) {
            return number * 2;
        }

        static boolean isGreaterThan5(int number) {
            return number > 5;
        }
    }


    public void loanPatternClick(View view) {
        log("loanPatternClick-------");
        withResource(Resource::operate);
    }

    void withResource(Consumer<Resource> consumer) {
        Resource resource = new Resource();
        try {
            consumer.accept(resource);
        } catch (Exception e) {
            log(e.toString());
        } finally {
            resource.dispose();
        }
    }

    class Resource {

        public Resource() {
            log("1. Opening resource");
        }

        public void operate() {
            log("2. Operating on resource");
            throw new RuntimeException("This is a crash");
        }

        public void dispose() {
            log("3. Disposing resource");
        }
    }

}
