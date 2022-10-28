package cs.vsu.ru.kravtsova_n_e.cubicsplineapp;

import java.util.ArrayList;
import java.util.List;

public class CubicSpline {
    List<List<Double>> data = new ArrayList<>();
    List<Double> a = new ArrayList<>();
    List<Double> b = new ArrayList<>();
    List<Double> c = new ArrayList<>();
    List<Double> d = new ArrayList<>();
    List<Double> ssd = new ArrayList<>(); //SplineSecondDerivatives - вторые производные от сплайна

    List<List<ArrayList>> splineValues = new ArrayList<>();

    public Double getData(int n, int m){
        return data.get(n).get(m);
    }

    public void addData(double x, double y){
        List<Double> tmpList = new ArrayList<>();
        tmpList.add(x);
        tmpList.add(y);
        data.add(tmpList);
    }

    public void interpolate() {
        calculateSplineSecondDerivatives();
        calculateCoefficients();
        //TODO: добавить вычисление самого сплайна
    }

    //Вычисление вторых производных кубического сплайна для всех участков
    void calculateSplineSecondDerivatives() {
        List<Double> bt = new ArrayList<>();
        List<Double> at = new ArrayList<>();
        List<Double> ct = new ArrayList<>();
        List<Double> r = new ArrayList<>();

        buildTridiagonalMatrix(at, bt, ct, r);

        List<Double> beta = new ArrayList<>();
        List<Double> rho = new ArrayList<>();
        beta.add(bt.get(0));
        rho.add(r.get(0));

        ssd.add(0.0);

        for (int i = 1; i < data.size(); i++) {
            beta.add(bt.get(i) - at.get(i) * ct.get(i - 1) / beta.get(i - 1));
            rho.add(r.get(i) - at.get(i) * rho.get(i - 1) / beta.get(i - 1));
            ssd.add(0.0);
        }

        ssd.set(data.size() - 1, rho.get(data.size() - 1) / beta.get(data.size() - 1));
        for (int i = 2; i < data.size() - 2; i++) {
            ssd.set(data.size() - i, (rho.get(data.size() - i) - ct.get(data.size() - i) * ssd.get(data.size() - i + 1)) / beta.get(data.size() - i));
        }
    }

    //Вычисление элементов тридиагональной матрицы T и вектора-столбца R
    void buildTridiagonalMatrix(List<Double> at, List<Double> bt, List<Double> ct, List<Double> r){
        bt.add(1.0);
        ct.add(getData(1, 0) - getData(0, 0));
        at.add(0.0);
        r.add(0.0);

        for (int i = 1; i < data.size() - 1; i++){
            bt.add(2 * (getData(i + 1, 0) - getData(i - 1, 0)));
            ct.add(getData(i + 1, 0) - getData(i, 0));
            at.add(getData(i, 0) - getData(i - 1, 0));
            double rtmp = (getData(i + 1, 1) - getData(i, 1))/(getData(i + 1, 0) - getData(i, 0));
            rtmp = rtmp - (getData(i, 1) - getData(i - 1, 1))/(getData(i, 0) - getData(i - 1, 0));
            r.add(6 * rtmp);
        }

        at.add(getData(data.size() - 1, 0) - getData(data.size()-2, 0));
        bt.add(1.0);
        ct.add(0.0);
        r.add(0.0);
    }

    //Вычисление коэффициентов кубического сплайна
    void calculateCoefficients(){
        for (int i = 0; i < data.size() - 1; i++) {
            d.add(getData(i, 1));
            b.add(ssd.get(i) / 2);
            a.add((ssd.get(i + 1) - ssd.get(i)) / (6 * (getData(i + 1, 0) - getData(i,0))));
            double ctmp = (getData(i + 1, 1) - getData(i,1)) / (getData(i + 1,0) - getData(i,0));
            ctmp = ctmp - ((getData(i + 1,0) - getData(i,0)) * (ssd.get(i + 1) + 2 * ssd.get(i)) / 6);
            c.add(ctmp);
        }
    }
}
