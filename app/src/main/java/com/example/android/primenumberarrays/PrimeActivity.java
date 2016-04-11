package com.example.android.primenumberarrays;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PrimeActivity extends AppCompatActivity {


    public static ArrayList<Integer> total_primes = new ArrayList<>();
    public String mlistArrays = new String();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);


    }

    //Дествие при нажатие на кнопку GO
    public void rangeAction(View view) {
        EditText first_number = (EditText) findViewById(R.id.first_number);
        EditText second_number = (EditText) findViewById(R.id.second_number);
        EditText module_number = (EditText) findViewById(R.id.module_number);
        TextView result = (TextView) findViewById(R.id.result);
        int a = Integer.parseInt(first_number.getText().toString());
        int b = Integer.parseInt(second_number.getText().toString());
        int p = Integer.parseInt(module_number.getText().toString());

        //Нахождение простых чисел в диапазоне от a до b
        calculate(a, b);


        // Предварительная проверка границ [a, b]
        if (a <= 0) {

            //Никогда не будет вызван, ввод разрещен только положительным числам
            Snackbar.make(view, "A > 0", Snackbar.LENGTH_SHORT).show();
        } else if (b <= a) Snackbar.make(view, "A < B", Snackbar.LENGTH_SHORT).show();
        else if (total_primes.size() == 0) {
            Snackbar.make(view, "Нет чисел", Snackbar.LENGTH_SHORT).show();
        } else if (p <= 1) {
            Snackbar.make(view, "P > 1", Snackbar.LENGTH_SHORT).show();
        } else {
            // Вычисление массивов при передаче значения из третьего поля EditText layout.activity_prime
            arraysGenerate(p);
            result.setText(mlistArrays);
        }
    }

    public String showArrays (List<List<Integer>> total_arays){
        String listOfArrays ="";
        for (int i=0; i<=total_arays.size()-1; i++){
            if(i < total_arays.size()-1) listOfArrays += total_arays.get(i) + "; ";
            if (i== total_arays.size()-1) listOfArrays += total_arays.get(i);
        }
        return listOfArrays;
    }

    public void initializeData(int result) {
        total_primes.add(result);
    }

    // Функция нахождения простых чисел, которой передаются два параметра a и b
    public void calculate(int a, int b) {

        total_primes.clear();

        // SieveOfAtkin
        int limit = b;
        boolean[] sieve = new boolean[limit + 1];
        int limitSqrt = (int) Math.sqrt((double) limit);


        Arrays.fill(sieve, false);
        // the sieve works only for integers > 3, so
        // set these trivially to their proper values
        sieve[0] = false;
        sieve[1] = false;
        sieve[2] = true;
        sieve[3] = true;

        for (int x = 1; x <= limitSqrt; x++) {
            for (int y = 1; y <= limitSqrt; y++) {
                // first quadratic using m = 12 and r in R1 = {r : 1, 5}
                int n = (4 * x * x) + (y * y);
                if (n <= limit && (n % 12 == 1 || n % 12 == 5)) {
                    sieve[n] = !sieve[n];
                }
                // second quadratic using m = 12 and r in R2 = {r : 7}
                n = (3 * x * x) + (y * y);
                if (n <= limit && (n % 12 == 7)) {
                    sieve[n] = !sieve[n];
                }
                // third quadratic using m = 12 and r in R3 = {r : 11}
                n = (3 * x * x) - (y * y);
                if (x > y && n <= limit && (n % 12 == 11)) {
                    sieve[n] = !sieve[n];
                } // end if
                // note that R1 union R2 union R3 is the set R
                // R = {r : 1, 5, 7, 11}
                // which is all values 0 < r < 12 where r is
                // a relative prime of 12
                // Thus all primes become candidates
            } // end for
        } // end for
        // remove all perfect squares since the quadratic
        // wheel factorization filter removes only some of them
        for (int n = 5; n <= limitSqrt; n++) {
            if (sieve[n]) {
                int x = n * n;
                for (int i = x; i <= limit; i += x) {
                    sieve[i] = false;
                } // end for
            } // end if
        } // end for

        for (int i = a; i <= b; i++) {
            if (sieve[i]) {
                initializeData(i);
            } // end if
        } // end for


    }

    public static void addPairToList(int size, ArrayList<Integer> tempArray, List<List<Integer>> list) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            temp.add(tempArray.get(i));

        }
        list.add(temp);
    }

    // Нахождение среднего арифметического модулей разностей всех элементов массива
    public void arraysGenerate(int p) {
        Random random = new Random();

        int count = 0;
        double result, temp_result = 0.0;
        double p_max = p + 1.0, p_min = p - 1.0;
        double e = 0.01;

        List<List<Integer>> total_arrays = new ArrayList<>();
        ArrayList<Integer> tempArrayList = new ArrayList<>();
        //Collections.reverse(total_primes);


        // Выбираем минимальное простое число для упрощения вычислений если найдется 1 модуль с разностью Р
        for (int i = 0; i < total_primes.size() - 1; i++) {
            tempArrayList.add(0, total_primes.get(i));
            for (int j = i + 1; j < total_primes.size(); j++) {
                temp_result = Math.abs(tempArrayList.get(0) - total_primes.get(j));
                if (temp_result == p) {
                    tempArrayList.add(1, total_primes.get(j));
                    addPairToList(tempArrayList.size(), tempArrayList, total_arrays);
                    tempArrayList.clear();
                    break;
                }
            }
            if (temp_result != p) tempArrayList.remove(0);
        }

        // Если прошло удачно и нашелся модуль то заберем первый, так как он гарантированно существует
        if (total_arrays.size() > 0) {
            tempArrayList.addAll(total_arrays.get(0));
        }

        // Если не нашолся модуль выполняющий наши условия, то прийдется генерировать рандомно модуль из 2 чисел.
        // Но за первое число возьмем минимальное простое

        if (total_arrays.size() == 0) {
            tempArrayList.add(total_primes.get(0));
            tempArrayList.add(total_primes.get(random.nextInt(total_primes.size())));

            for (int i = 0; i < tempArrayList.size() - 1; i++) {
                for (int j = i + 1; j < tempArrayList.size(); j++) {
                    temp_result = temp_result + Math.abs(tempArrayList.get(i) - tempArrayList.get(j));
                    count++;
                }
            }
            result = temp_result / count;

            if (result == p) {
                total_arrays.add(tempArrayList);
            }

        }


        int index = total_primes.indexOf(tempArrayList.get(tempArrayList.size() - 1));
        int size = total_primes.size();
        result = temp_result / count;


        while (total_arrays.size() < p) {
            // Заберем первый массив из списка, так как он гарантированно существует, он позволит не перейти в бесконечгый цикл корневому while
            // Который следит за количеством массивов не больше Р
            tempArrayList.clear();
            tempArrayList.addAll(total_arrays.get(0));
            result = 0.0;
            while (!(result >= p_min + e && result <= p_max + e)) {
                temp_result = 0.0;
                count = 0;
                if (tempArrayList.get(tempArrayList.size() - 1) == total_primes.get(size - 1)) {
                    tempArrayList.add(total_primes.get(random.nextInt(size - 1)));
                } else if (result >= p + 0.5) {
                    tempArrayList.add(total_primes.get(random.nextInt(index)));
                } else if (result < p - 0.5) {
                    tempArrayList.add(total_primes.get(random.nextInt(size - index)+index));
                }
                for (int i = 0; i < tempArrayList.size() - 1; i++) {
                    for (int j = i + 1; j < tempArrayList.size(); j++) {
                        temp_result += Math.abs(tempArrayList.get(i) - tempArrayList.get(j));
                        count++;
                    }
                }
                result = temp_result / count;
            }
            addPairToList(tempArrayList.size(), tempArrayList, total_arrays);
        }

        mlistArrays = showArrays(total_arrays);
    }
}

