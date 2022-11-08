package cs.vsu.ru.kravtsova_n_e.cubicsplineapp;

import java.util.ArrayList;
import java.util.List;

public class CubicSpline {
    List<Double> x = new ArrayList<>();
    List<Double> y = new ArrayList<>();

    List<Double> a = new ArrayList<>();
    List<Double> b = new ArrayList<>();
    List<Double> c = new ArrayList<>();
    List<Double> d = new ArrayList<>();

    List<Double> splineXValues = new ArrayList<>();
    List<Double> splineYValues = new ArrayList<>();

    public List<Double> getSplineXValues(){
        return splineXValues;
    }

    public List<Double> getSplineYValues() {
        return splineYValues;
    }

    public void addData(double x, double y){
        this.x.add(x);
        this.y.add(y);
        if (this.x.size() > 1) {
            sortPoints();
        }
    }

    private void sortPoints(){
        for (int j = 0; j < x.size(); j++) {
            for (int i = 0; i < x.size() - 1; i++) {
                if (x.get(i) > x.get(i + 1)) {
                    double tmp = x.get(i + 1);
                    x.set(i + 1, x.get(i));
                    x.set(i, tmp);
                    tmp = y.get(i + 1);
                    y.set(i + 1, y.get(i));
                    y.set(i, tmp);
                }
            }
        }
    }

    public void interpolate() {
        List<Double> splineXValues = new ArrayList<>();
        List<Double> splineYValues = new ArrayList<>();

        calculateCoefficients(calculateSplineSecondDerivatives());
        double dx = 0.01;
        double x_ = x.get(0);
        while (x_ <= x.get(x.size()-1)) {
            int i = 0;
            while (x.get(i + 1) < x_) {
                i++;
            }
            double xdiff = x_ - x.get(i);
            splineXValues.add(x_);
            splineYValues.add(a.get(i) * Math.pow(xdiff, 3) + b.get(i) * Math.pow(xdiff, 2) + c.get(i) * xdiff + d.get(i));
            x_ += dx;
        }

        this.splineXValues = splineXValues;
        this.splineYValues = splineYValues;
    }

    //Вычисление вторых производных кубического сплайна для всех участков
    private List<Double> calculateSplineSecondDerivatives() {
        List<Double> bt = new ArrayList<>();
        List<Double> at = new ArrayList<>();
        List<Double> ct = new ArrayList<>();
        List<Double> r = new ArrayList<>();

        buildTridiagonalMatrix(at, bt, ct, r);

        List<Double> beta = new ArrayList<>();
        List<Double> rho = new ArrayList<>();
        List<Double> ssd = new ArrayList<>();
        beta.add(bt.get(0));
        rho.add(r.get(0));
        ssd.add(0.0);

        for (int i = 1; i < x.size(); i++) {
            beta.add(bt.get(i) - at.get(i) * ct.get(i - 1) / beta.get(i - 1));
            rho.add(r.get(i) - at.get(i) * rho.get(i - 1) / beta.get(i - 1));
            ssd.add(0.0);
        }

        ssd.set(x.size() - 1, rho.get(x.size() - 1) / beta.get(x.size() - 1));
        for (int i = 2; i < x.size() - 2; i++) {
            ssd.set(x.size() - i, (rho.get(x.size() - i) - ct.get(x.size() - i) * ssd.get(x.size() - i + 1)) / beta.get(x.size() - i));
        }

        return ssd;
    }

    //Вычисление элементов тридиагональной матрицы T и вектора-столбца R
    private void buildTridiagonalMatrix(List<Double> at, List<Double> bt, List<Double> ct, List<Double> r){
        bt.add(1.0);
        ct.add(x.get(1) - x.get(0));
        at.add(0.0);
        r.add(0.0);

        for (int i = 1; i < x.size() - 1; i++){
            bt.add(2 * (x.get(i + 1) - x.get(i - 1)));
            ct.add(x.get(i + 1) - x.get(i));
            at.add(x.get(i) - x.get(i - 1));
            double rtmp = (y.get(i + 1) - y.get(i))/(x.get(i + 1) - x.get(i));
            rtmp = rtmp - (y.get(i) - y.get(i - 1))/(x.get(i) - x.get(i - 1));
            r.add(6 * rtmp);
        }

        at.add(x.get(x.size() - 1) - x.get(x.size() - 2));
        bt.add(1.0);
        ct.add(0.0);
        r.add(0.0);
    }

    //Вычисление коэффициентов кубического сплайна
    private void calculateCoefficients(List<Double> ssd){
        List<Double> a = new ArrayList<>();
        List<Double> b = new ArrayList<>();
        List<Double> c = new ArrayList<>();
        List<Double> d = new ArrayList<>();
        for (int i = 0; i < y.size() - 1; i++) {
            d.add(y.get(i));
            b.add(ssd.get(i) / 2);
            a.add((ssd.get(i + 1) - ssd.get(i)) / (6 * (x.get(i + 1) - x.get(i))));
            double ctmp = (y.get(i + 1) - y.get(i)) / (x.get(i + 1) - x.get(i));
            ctmp = ctmp - ((x.get(i + 1) - x.get(i)) * (ssd.get(i + 1) + 2 * ssd.get(i)) / 6);
            c.add(ctmp);
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
}
