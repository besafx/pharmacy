package com.besafx.app.util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author MazenB
 */
public class ArabicLiteralNumberParser {

    private static final String ZERO = "صفر";

    private static final String ONE = "واحد";

    private static final String TWO = "إثنان";

    private static final String THREE = "ثلاثة";

    private static final String FOUR = "أربعة";

    private static final String FIVE = "خمسة";

    private static final String SIX = "ستة";

    private static final String SEVEN = "سبعة";

    private static final String EIGHT = "ثمانية";

    private static final String NINE = "تسعة";

    private static final String TEN = "عشرة";

    private static final String TWENTY = "عشرون";

    private static final String THIRTY = "ثلاثون";

    private static final String FOURTY = "أربعون";

    private static final String FIFTY = "خمسون";

    private static final String SIXTY = "ستون";

    private static final String SEVENTY = "سبعون";

    private static final String EIGHTY = "ثمانون";

    private static final String NINETY = "تسعون";

    private static final String HUNDRED = "مائة";

    private static final String THOUSAND = "الف";

    private static final String MILLION = "مليون";

    private static final String MILLIARD = "مليار";

    private static final String BILLION = "بليون";

    private static final String BILLIARD = "بليار";

    private static final String TRILLION = "تريليون";

    private static final Map<Long, String> namesMap;

    static {
        namesMap = new HashMap<Long, String>();
        namesMap.put(0l, ZERO);
        namesMap.put(1l, ONE);
        namesMap.put(2l, TWO);
        namesMap.put(3l, THREE);
        namesMap.put(4l, FOUR);
        namesMap.put(5l, FIVE);
        namesMap.put(6l, SIX);
        namesMap.put(7l, SEVEN);
        namesMap.put(8l, EIGHT);
        namesMap.put(9l, NINE);
        namesMap.put(10l, TEN);
        namesMap.put(20l, TWENTY);
        namesMap.put(30l, THIRTY);
        namesMap.put(40l, FOURTY);
        namesMap.put(50l, FIFTY);
        namesMap.put(60l, SIXTY);
        namesMap.put(70l, SEVENTY);
        namesMap.put(80l, EIGHTY);
        namesMap.put(90l, NINETY);
        namesMap.put(100l, HUNDRED);
        namesMap.put(1000l, THOUSAND);
        namesMap.put(1000000l, MILLION);
        namesMap.put(1000000000l, MILLIARD);//billion in short scale (US)
        namesMap.put(1000000000000l, BILLION);//trillion //
        namesMap.put(1000000000000000l, BILLIARD);//quadrllion //
        namesMap.put(1000000000000000000l, TRILLION);//quintillion //
    }

    private static String parse(long a) {
        StringBuffer buf = new StringBuffer(Long.valueOf(a).toString());
        buf.reverse();
        int index = 0;
        boolean negative;
        int len = (negative = buf.charAt(buf.length() - 1) == '-') ? buf.length() - 1 : buf.length();
        String[] name = new String[len];
        long unitValue = 0;
        while (index < len) {
            Long n = Long.valueOf(String.valueOf(buf.charAt(index)));
            int decimalPos = index % 3;
            if (decimalPos == 0) {
                unitValue = (Double.valueOf(Math.pow(10, index)).longValue());
            }
            int decimalPlace = (Double.valueOf(Math.pow(10, decimalPos)).intValue());
            switch (decimalPlace) {
                case 1:
                    if (unitValue > 1l && index + 1 == len) {
                        switch (n.intValue()) {
                            case 1:
                                name[index] = namesMap.get(unitValue);
                                break;
                            case 2:
                                name[index] = namesMap.get(unitValue).concat("ان");
                                break;
                            default:
                                if (unitValue == 1000l) {
                                    name[index] = namesMap.get(n) + " " + "الاف";
                                } else if (unitValue == 1000000l) {
                                    name[index] = namesMap.get(n) + " " + "ملايين";
                                } else {
                                    name[index] = namesMap.get(n) + " " + namesMap.get(unitValue) + "ات";
                                }
                        }
                    } else {
                        name[index] = namesMap.get(n);
                    }
                    break;
                case 10:
                    String tmp = name[index - 1];
                    if (n == 1l) {
                        if (tmp.equals(ONE)) {
                            tmp = tmp.substring(1, tmp.length());
                        } else if (tmp.equals(TWO)) {
                            tmp = tmp.substring(0, tmp.length() - 1);
                        }
                    }
                    if (unitValue > 1l && index + 1 == len) {
                        if (n == 1 && tmp.equals(ZERO)) {
                            if (unitValue == 1000l) {
                                name[index] = TEN + "الاف ";
                            } else if (unitValue == 1000000l) {
                                name[index] = TEN + "ملايين ";
                            } else {
                                name[index] = TEN + " " + namesMap.get(unitValue) + "ات";
                            }
                        } else {
                            name[index] = namesMap.get(n * 10l) + " " + namesMap.get(unitValue);
                        }
                    } else {
                        name[index] = namesMap.get(n * 10l);
                    }
                    if (n != 0l) {
                        name[index - 1] = name[index];
                        name[index] = tmp;
                    }
                    break;
                case 100:
                    String s;
                    if (n > 2) {
                        s = namesMap.get(n);
                        s = s.substring(0, s.length() - (n == 8 ? 2 : 1)) + HUNDRED;
                    } else {
                        s = n == 2 ? "مائتان" : namesMap.get(n * 100);
                    }
                    if (unitValue > 1l && !name[index - 2].equals(ZERO)) {
                        Iterator i = namesMap.keySet().iterator();
                        while (i.hasNext()) {
                            Long val = (Long) i.next();
                            if (namesMap.get(val).equals(name[index - 2])) {
                                if (val > 2l && val <= 10l) {
                                    if (unitValue == 1000l) {
                                        name[index - 2] = name[index - 2] + " " + "الاف";
                                    } else if (unitValue == 1000000l) {
                                        name[index - 2] = name[index - 2] + " " + "ملايين";
                                    } else {
                                        name[index - 2] = name[index - 2] + " " + namesMap.get(unitValue) + "ات";
                                    }

                                } else {
                                    name[index - 2] = name[index - 2] + " " + namesMap.get(unitValue);
                                }
                                break;
                            }
                        }

                    } else if (unitValue > 1l && n != 0) {
                        s = s + " " + namesMap.get(unitValue);
                    }
                    name[index] = s;

            }
            index++;
        }
        String s = "";
        for (int i = 0; i < len; i++) {
            if (name[i].equals(ZERO)) {
                continue;
            }
            name[i] = name[i].trim();
            s = name[i] + (!s.isEmpty() && !(s.startsWith(TEN) && !name[i - 1].equals(ZERO)) ? " و" : " ") + s;

        }
        return s.isEmpty() ? ZERO : (negative ? "سالب " + s : s).trim();
    }

    public static String literalValueOf(Number a) {
        if (a.doubleValue() > Long.MAX_VALUE) {
            return " اكبر من " + parse(Long.MAX_VALUE);
        }
        a = String.valueOf(a).toLowerCase().contains("e") ? a.longValue() : a;
        String[] array = String.valueOf(a).split("\\.");
        long i = Long.valueOf(array[0]);
        long f = array.length == 2 ? Long.valueOf(array[1]) : 0;
        int fractSize = f > 0 ? array[1].length() : 0;
        String integralPart = (i != 0 || f == 0) ? parse(i) : "";
        String fractionalPart = f > 0 ? parse(f) + " من " + parse(Double.valueOf(Math.pow(10, fractSize)).longValue()) : "";
        return integralPart + (f * i != 0 ? " و " : "") + fractionalPart;
    }
}

