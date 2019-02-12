package hndt.radiolibs.util;

import java.io.Serializable;
import java.util.Collection;

public class Validator implements Serializable {

    private static Validator biz = null;

    private Validator() {
    }

    public static Validator getInstance() {
        if (biz == null) {
            biz = new Validator();
        }
        return biz;
    }

    public String byOrder(Assert... statements) {
        String msg = null;
        for (Assert stat : statements) {
            msg = test(stat.getLabel(), stat.getValue(), stat.getMin(), stat.getMax(), stat.getTip());
            if (msg != null) {
                break;
            }
        }
        return msg;
    }

    public String test(String label, Object value, double min, double max) {
        return test(label, value, min, max, null);
    }

    public String test(String label, Object value, double min, double max, String tip) {
        String msg = null;

        if (value == null) {
            msg = label + "不能为空";
        } else if (value instanceof String) {
            int int_min = (int) min;
            int int_max = (int) max;
            String val = (String) value;
            if (val.trim().length() == 0) {
                msg = label + "不能为空";
                return msg;
            }
            if ((int_min > 0 && val.length() < int_min) || (int_max > 0 && val.length() > int_max)) {
                msg = (tip == null) ? (label + "的字数必需在" + int_min + "和" + int_max + "之间") : tip;
            }
        } else if (value instanceof Long || value instanceof Integer) {
            long val = new Long(value.toString()).longValue();
            int int_min = (int) min;
            int int_max = (int) max;
            if ((int_min > 0 && val < int_min) || (int_max > 0 && val > int_max)) {
                msg = (tip == null) ? (label + "的值必需在" + int_min + "和" + int_max + "之间") : tip;
            }
        } else if (value instanceof Number) {
            Number val = (Number) value;
            if ((min > val.doubleValue()) || (max < val.doubleValue())) {
                msg = (tip == null) ? (label + "的值必需在" + min + "和" + max + "之间") : tip;
            }
        } else if (value instanceof Collection) {
            int int_min = (int) min;
            int int_max = (int) max;
            Collection val = (Collection) value;
            if (val.isEmpty()) {
                msg = label + "不能为空";
                return msg;
            }
            if ((int_min > 0 && val.size() < int_min) || (int_max > 0 && val.size() > int_max)) {
                msg = (tip == null) ? (label + "的值必需在" + int_min + "和" + int_max + "之间") : tip;
            }
        }
        return msg;
    }

    public static class Assert {

        String label;
        Object value;
        double min;
        double max;
        String tip;

        public Assert(String label, Object value, double min, double max, String tip) {
            this.label = label;
            this.value = value;
            this.min = min;
            this.max = max;
            this.tip = tip;
        }

        public Assert(String label, Object value, double min, double max) {
            this.label = label;
            this.value = value;
            this.min = min;
            this.max = max;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

    }

}
