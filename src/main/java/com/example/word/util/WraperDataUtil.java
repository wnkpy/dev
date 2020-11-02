package com.example.word.util;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 数据类型 转化类
 * <p>
 * 把 常用的数据类型 封装成 jfreechart - dataset
 *
 * @author xiaorong.zhu
 */
public class WraperDataUtil {


    /**
     * 格式化数据为饼图数据源
     *
     * @param map
     * @return
     */
    public static DefaultPieDataset formatPieDpd(Map<String, Long> map) {
        DefaultPieDataset dpd = new DefaultPieDataset();
        if (map.isEmpty()) {
            dpd.setValue("无数据", 0.001);
            return dpd;
        }
        Iterator<?> iter = map.keySet().iterator();

        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (key == null || "".equals(key)) {
                continue;
            }

            dpd.setValue(key, map.get(key));
        }
        return dpd;

    }


    /**
     * 格式化数据为柱状图数据源
     *
     * @param map
     * @return
     */
    public static DefaultCategoryDataset formatBarDcd(Map<String, Long> map, String legend) {
        Iterator<?> iter = map.keySet().iterator();
        DefaultCategoryDataset dcd = new DefaultCategoryDataset();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (key != null && !key.equals("")) {
                dcd.addValue(map.get(key), legend, key);
            }

        }
        return dcd;
    }

    /**
     * 时间序列图 转化，此时 为 一条线
     * 多线 增加TimeSeries
     *
     * @param field
     * @param map
     * @return
     */
    public static TimeSeriesCollection timeData(String field, Map<String, String> map) {
        Iterator<?> iter = map.keySet().iterator();
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries s1 = new TimeSeries(field, Day.class);
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Date x = null;
            String type = "yyyy-MM-dd";
            if (key != null && !key.equals("")) {
                x = getDataByStr(key, type);
                s1.addOrUpdate(new Day(x), Double.parseDouble(map.get(key)));
            }

        }
        dataset.addSeries(s1);
        return dataset;
    }

    /**
     * 转化时间
     *
     * @param timeStr
     * @param type
     * @return
     */
    public static Date getDataByStr(String timeStr, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}