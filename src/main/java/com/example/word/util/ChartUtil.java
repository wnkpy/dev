package com.example.word.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.Rotation;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 图片工具类
 *
 * @author xiaorong.zhu
 */
public class ChartUtil {
    /**
     * 饼图
     *
     * @param title    图表题
     * @param dataset  数据源
     * @param legend   图例
     * @param tooltips 提示
     * @param locale   是否指定URL
     * @return
     */
    public static JFreeChart getPieChart(String title, DefaultPieDataset dataset, boolean legend, boolean tooltips, boolean locale, RectangleEdge r) {
        ChartFactory.setChartTheme(getChartTheme());
        //title:图表题; dataset:数据源; legend:是否显示图例;tooltips:是否显示tooltip;locale:是否指定url
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, legend, tooltips, locale);
        //设置图标样式
        setChart(chart);
        //图例样式
        setLegendTitle(chart, r);
        //得到饼图的plot对象
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        //设置旋转角度
        plot.setStartAngle(180.0);

        //设置旋转方向，Rotation.CLOCKWISE)为顺时针。
        plot.setDirection(Rotation.CLOCKWISE);
        //设置图表透明图0.0~1.0范围。0.0为完全透明，1.0为完全不透明。
        plot.setForegroundAlpha(0.5F);
        // plot.setNoDataMessage("无数据");
        //扇区标签
        setLabel(plot);
        //扇区颜色
        setSection(plot, dataset);
        return chart;
    }

    /**
     * 柱状图
     *
     * @param dataset  数据集
     * @param isSingle
     * @param barColor 柱颜色
     * @return
     */
    public static JFreeChart getBarChart(String title, CategoryDataset dataset, boolean isSingle, PlotOrientation p, Color barColor, CategoryLabelPositions c, RectangleEdge r) {
        ChartFactory.setChartTheme(getChartTheme());
        JFreeChart chart = null;
        if (isSingle) {
            chart = ChartFactory.createBarChart3D(title, "", "", dataset, p, false, false, false);
        } else {
            chart = ChartFactory.createStackedBarChart3D(title, "", "", dataset, p, false, false, false);
        }

        setChart(chart);
        //图形的绘制结构对象
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        setPlot(plot);
        //设置x轴样式
        setAxis(plot, c);
        //设置y轴样式
        setValueAxis(plot);
        //设置图例
        setLegendTitle(chart, r);
        //设置y轴刻度
        setNumberAxis(plot, false);
        //设置Bar
        setBar(plot, isSingle, dataset, barColor);

        return chart;
    }


    public static JFreeChart getMultiBarChart(String title, CategoryDataset dataset, CategoryLabelPositions c, int paint, RectangleEdge r) {
        ChartFactory.setChartTheme(getChartTheme());
        JFreeChart chart = ChartFactory.createBarChart(title,// 标题
                "",// x轴
                "",// y轴
                dataset,// 数据
                PlotOrientation.VERTICAL,// 定位，VERTICAL：垂直
                false,// 是否显示图例注释(对于简单的柱状图必须是false)
                false,// 是否生成工具//没用过
                false);// 是否生成URL链接//没用过
        setChart(chart);
        //图形的绘制结构对象
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        setPlot(plot);
        //设置x轴样式
        setAxis(plot, c);
        //设置y轴样式
        setValueAxis(plot);
        //设置图例
        setLegendTitle(chart, r);
        //设置y轴刻度
        setNumberAxis(plot, false);
        //设置Bar
        setRender(plot, paint);
        return chart;

    }

    public static void setRender(CategoryPlot plot, int paint) {
        BarRenderer renderer1 = new BarRenderer();// 设置柱子的相关属性
        renderer1.setMinimumBarLength(0.5);
        renderer1.setItemMargin(0.2);
        // renderer1.set
        // 是否显示阴影
        renderer1.setShadowVisible(false);
        plot.setRenderer(renderer1);
        plot.setBackgroundAlpha((float) 0.1); // 数据区的背景透明度（0.0～1.0）
        renderer1.setSeriesPaint(0, new Color(74, 126, 187));
        if (paint == 1) {
            renderer1.setSeriesPaint(1, new Color(190, 75, 72));
        }
        if (paint == 2) {
            renderer1.setSeriesPaint(1, new Color(190, 75, 72));
            renderer1.setSeriesPaint(2, new Color(152, 185, 84));
        }

    }


    /**
     * 折线图
     *
     * @param dataset    数据集
     * @param numberAxis y轴精度
     * @param isGroup
     * @return
     */
    public static JFreeChart getLineChart(String title, DefaultCategoryDataset dataset, boolean numberAxis, boolean isGroup, CategoryLabelPositions c, RectangleEdge r) {
        ChartFactory.setChartTheme(getChartTheme());
        JFreeChart chart = ChartFactory.createLineChart(title, "", "", dataset, PlotOrientation.VERTICAL, false, true, false);
        setChart(chart);
        //图形的绘制结构对象
        CategoryPlot plot = chart.getCategoryPlot();
        setPlot(plot);
        //设置x轴
        setAxis(plot, c);
        //设置y轴
        setValueAxis(plot);
        //设置y轴刻度显示样式
        setNumberAxis(plot, numberAxis);
        //设置折线样式
        setRender(plot, isGroup);
        //设置图例
        setLegendTitle(chart, r);

        return chart;

    }

    /**
     * 设置网格
     *
     * @param plot
     */
    public static void setPlot(CategoryPlot plot) {
        //设置网格背景色
        plot.setBackgroundPaint(Color.WHITE);
        //设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.WHITE);
        //设置网格横线颜色
        plot.setRangeGridlinePaint(Color.BLACK);
        //设置数据区的边界线条颜色
        plot.setOutlinePaint(Color.WHITE);

    }

    /**
     * 设置y轴刻度显示样式
     *
     * @param plot
     * @param numberAxis
     */
    public static void setNumberAxis(CategoryPlot plot, boolean numberAxis) {
        Font kfont = new Font("宋体", Font.PLAIN, 12);
        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();

        String numAxis = "#0";
        if (numberAxis || numberaxis.getUpperBound() <= 6) {
            //y轴显示精度
            numAxis = "#0.00";
        }
        numberaxis.setNumberFormatOverride(new DecimalFormat(numAxis));
        numberaxis.setTickLabelFont(kfont);
    }

    /**
     * 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
     *
     * @param plot
     */
    public static void setRender(CategoryPlot plot, boolean isGroup) {
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        //显示折点数据
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        // 定义series为"First"的（即series1）点之间的连线
        // 第几个数据集 ; 线的宽度 ; 控制虚线直线
        renderer.setSeriesStroke(0, new BasicStroke(3.0F, 1, 1, 1.0F, new float[]{10F, 0F}, 1F));
        //设置颜色
        renderer.setSeriesPaint(0, new Color(74, 126, 187));
        if (isGroup) {
            renderer.setSeriesPaint(1, new Color(190, 75, 72));
            renderer.setSeriesPaint(2, new Color(152, 185, 84));
            renderer.setSeriesStroke(1, new BasicStroke(3.0F, 1, 1, 1.0F, new float[]{10F, 0F}, 1F));
            renderer.setSeriesStroke(2, new BasicStroke(3.0F, 1, 1, 1.0F, new float[]{10F, 0F}, 1F));
        }

    }

    /**
     * 设置x轴
     */
    public static void setAxis(CategoryPlot plot, CategoryLabelPositions c) {
        // 配置字体
        Font xfont = new Font("宋体", Font.PLAIN, 12);// X轴
        //X轴
        CategoryAxis domainAxis = plot.getDomainAxis();
        //x轴的标题文字是否显示
        domainAxis.setTickLabelsVisible(true);
        //设置x轴标题字体
        domainAxis.setLabelFont(xfont);
        //设置x轴字体
        domainAxis.setTickLabelFont(xfont);
        //设置字体颜色
        domainAxis.setTickLabelPaint(Color.BLACK);

        //x轴label斜显示
        domainAxis.setCategoryLabelPositions(c);
        //分类轴编剧，同种类型之间的距离
        domainAxis.setCategoryMargin(0);
        //分类轴下（左）边距,就是离左边的距离
        domainAxis.setLowerMargin(0.1);
        //分类轴下（右）边距,就是离最右边的距离
        domainAxis.setUpperMargin(0.1);


    }

    /**
     * 自定义x轴时间刻度
     *
     * @return
     */
    public static JFreeChart createChart(String title, int unit, TimeSeriesCollection dataset, RectangleEdge rs) {
        ChartFactory.setChartTheme(getChartTheme());
        JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(
                title,//图表名
                "",//横轴标签文字
                "",//纵轴标签文字
                dataset,//图表的数据集合
                true,//是否显示图表中每条数据序列的说明
                false,//是否显示工具提示
                false);//是否显示图表中设置的url网络连接

        //XYPlot图表区域的设置对象，用来设置图表的一些显示属性

        XYPlot xyPlot = (XYPlot) jFreeChart.getPlot();
        setXYPlot(xyPlot);
        setXYValueAxis(xyPlot);
        setDateAxis(xyPlot, unit);
        setChart(jFreeChart);


        //设置图例
        setLegendTitle(jFreeChart, rs);
        return jFreeChart;
    }

    /**
     * 设置时序图Axis
     */
    public static void setDateAxis(XYPlot xyPlot, int unit) {
        //DateAxis是显示样式设置对象
        DateAxis dateAxis = (DateAxis) xyPlot.getDomainAxis();
        SimpleDateFormat frm = new SimpleDateFormat("yyyy年MM月dd日");
        //设置时间间隔为1天
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, unit, frm));
        // 配置字体
        Font xfont = new Font("宋体", Font.PLAIN, 10);// X轴

        //x轴的标题文字是否显示
        dateAxis.setTickLabelsVisible(true);
        // dateAxis.setAutoTickUnitSelection(true);
        //设置x轴标题字体
        dateAxis.setLabelFont(xfont);
        // dateAxis.setVerticalTickLabels(true);
        //设置x轴字体
        dateAxis.setTickLabelFont(xfont);
        dateAxis.setDateFormatOverride(new SimpleDateFormat("MM.dd"));
        //设置字体颜色
        // ((CategoryAxis) dateAxis).setCategoryLabelPositions(c);
        //分类轴编剧，同种类型之间的距离
        //  dateAxis.setCategoryMargin(0);
        //分类轴下（左）边距,就是离左边的距离
        //dateAxis.setLowerMargin(0.1);
        //分类轴下（右）边距,就是离最右边的距离
        dateAxis.setUpperMargin(0.1);
        dateAxis.setAutoRange(true);

    }

    /**
     * 设置时序图
     */
    public static void setXYPlot(XYPlot plot) {
        //设置网格背景色
        plot.setBackgroundPaint(Color.WHITE);
        //设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.WHITE);
        //设置网格横线颜色
        plot.setRangeGridlinePaint(Color.BLACK);
        //设置数据区的边界线条颜色
        plot.setOutlinePaint(Color.WHITE);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(false);
        }
    }

    /**
     * 设置y轴
     */
    public static void setXYValueAxis(XYPlot plot) {
        // 配置字体
        Font yfont = new Font("宋体", Font.CENTER_BASELINE, 20);// Y轴
        //y轴
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        //隐藏y轴的刻度标签
        rangeAxis.setTickLabelsVisible(true);
        //设置Y轴标题字体
        rangeAxis.setLabelFont(yfont);
        //标记线是否显示
        rangeAxis.setMinorTickMarksVisible(false);
        //设置y轴字体角度(1.6的时候是水平)
        rangeAxis.setLabelAngle(1.6);
        //设置字体颜色
        rangeAxis.setLabelPaint(Color.BLACK);
        rangeAxis.setLowerBound(0);
        Font kfont = new Font("宋体", Font.PLAIN, 12);
        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();

        String numAxis = "#0";
        if (numberaxis.getUpperBound() <= 6) {
            //y轴显示精度
            numAxis = "#0.00";
        }
        numberaxis.setNumberFormatOverride(new DecimalFormat(numAxis));
        numberaxis.setTickLabelFont(kfont);

    }

    /**
     * 设置y轴
     */
    public static void setValueAxis(CategoryPlot plot) {
        // 配置字体
        Font yfont = new Font("宋体", Font.CENTER_BASELINE, 20);// Y轴
        //y轴
        ValueAxis rangeAxis = plot.getRangeAxis();
        //隐藏y轴的刻度标签
        rangeAxis.setTickLabelsVisible(true);
        //设置Y轴标题字体
        rangeAxis.setLabelFont(yfont);
        //标记线是否显示
        rangeAxis.setMinorTickMarksVisible(false);
        //设置y轴字体角度(1.6的时候是水平)
        rangeAxis.setLabelAngle(1.6);
        //设置字体颜色
        rangeAxis.setLabelPaint(Color.BLACK);
        rangeAxis.setLowerBound(0);

    }

    /**
     * 设置Bar
     */
    public static void setBar(CategoryPlot plot, boolean isSingle, CategoryDataset dataset, Color barColor) {
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        //设置Bar的颜色
        //设置每个Bar之间的间隔
        renderer.setItemMargin(0.1f);
        //设置每个Bar的最大宽度
        renderer.setMaximumBarWidth(0.05f);
        //设置Bar是否显示阴影
        renderer.setShadowVisible(false);
        if (isSingle) {
            for (int i = 0; i < dataset.getRowKeys().size(); i++) {
                renderer.setSeriesPaint(i, barColor);
            }
        }

    }

    /**
     * 设置扇区颜色
     *
     * @param plot
     */
    public static void setSection(PiePlot3D plot, DefaultPieDataset dataset) {
        if (dataset != null) {
            List<String> list = dataset.getKeys();
            for (Object str : list) {
                if (str.equals("违法行为")) {
                    plot.setSectionPaint("违法行为", new Color(255, 0, 0));
                }
                if (str.equals("违规行为")) {
                    plot.setSectionPaint("违规行为", new Color(255, 165, 0));
                }
                if (str.equals("不明确行为")) {
                    plot.setSectionPaint("不明确行为", new Color(188, 238, 104));
                }
                if (str.equals("常规行为")) {
                    plot.setSectionPaint("常规行为", new Color(67, 205, 128));
                }
                if (str.equals("无数据")) {
                    plot.setSectionPaint("无数据", Color.WHITE);
                    plot.setLabelGenerator(null);
                }
            }
        }
    }

    //设置图标样式
    public static void setChart(JFreeChart chart) {
        //设置字体清晰
        chart.setTextAntiAlias(false);
        //图片背景色
        chart.setBackgroundPaint(Color.white);
        //边界线条是否可见
        chart.setBorderVisible(true);
        //边界线条笔触
        chart.setBorderStroke(new BasicStroke(0.2f));

    }

    /**
     * 设置扇区标签格式
     *
     * @param plot
     */
    public static void setLabel(PiePlot3D plot) {
        //设置扇区标签显示格式 {0}:关键字 ;{1}：数值;{2}:百分比
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1},{2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        //设置扇区标签颜色
        // plot.setLabelBackgroundPaint(new Color(220, 220, 220));
        //设置扇区标签字体
        plot.setLabelFont(new Font("宋体", Font.PLAIN, 10));
        //设置扇区透明度(0.0-1.0)
        plot.setForegroundAlpha(1f);
        //饼图是否一定是正圆
        plot.setCircular(true);
        //设置扇区背景颜色
        plot.setBackgroundPaint(Color.WHITE);
        //设置绘图面板外边的填充颜色
        plot.setOutlinePaint(Color.WHITE);
        //设置绘图面板阴影的填充色
        plot.setShadowPaint(Color.WHITE);
    }

    /**
     * 设置图例
     *
     * @param chart
     */
    public static void setLegendTitle(JFreeChart chart, RectangleEdge r) {
        Font kfont = new Font("宋体", Font.PLAIN, 12);
        LegendTitle legendTitle = new LegendTitle(chart.getPlot());
        //设置四周的边距
        legendTitle.setBorder(0, 0, 0, 0);
        //设置图例位置
        legendTitle.setPosition(r);
        //设置外边距
        legendTitle.setMargin(5, 5, 5, 5);
        //设置字体样式
        legendTitle.setItemFont(kfont);
        //移除默认的legend
        chart.removeLegend();
        //添加图例
        chart.addLegend(legendTitle);

    }


    /**
     * 生成饼图图片
     *
     * @param dataset 数据集
     * @param url     图片存储路径
     * @param width   图片宽度
     * @param height  图片高度
     */
    public static void getPieChartImage(DefaultPieDataset dataset, String url, int width, int height, RectangleEdge r) {
        JFreeChart chart = getPieChart("", dataset, true, true, false, r);
        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream(url);
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, width, height);
            fos_jpg.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos_jpg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成柱状图图片
     *
     * @param dataset  数据集
     * @param url      图片存储路径
     * @param width    图片宽度
     * @param height   图片高度
     * @param isSingle 是否单数值显示
     * @param barColor 柱子颜色
     */
    public static void getBarChartImages(String title, CategoryDataset dataset, String url, int width, int height, PlotOrientation p, boolean isSingle, Color barColor, CategoryLabelPositions c, RectangleEdge r) {
        JFreeChart chart = getBarChart(title, dataset, isSingle, p, barColor, c, r);
        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream(url);
            ChartUtilities.writeChartAsJPEG(fos_jpg, 1f, chart, width, height, null);
            fos_jpg.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos_jpg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 多柱图
     *
     * @param title
     * @param dataset
     * @param url
     * @param width
     * @param height
     * @param p
     * @param paint
     * @param c
     * @param r
     */
    public static void getMultiBarChartImages(String title, CategoryDataset dataset, String url, int width, int height, PlotOrientation p, int paint, CategoryLabelPositions c, RectangleEdge r) {
        JFreeChart chart = getMultiBarChart(title, dataset, c, paint, r);
        FileOutputStream fos_jpg = null;
        try {
            fos_jpg = new FileOutputStream(url);
            ChartUtilities.writeChartAsJPEG(fos_jpg, 1f, chart, width, height, null);
            fos_jpg.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos_jpg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 生成折线图图片
     *
     * @param url        保存路径
     * @param dataset    数据集
     * @param isGroup
     * @param numberAxis
     * @param width      图片宽度
     * @param height     图片高度
     */
    public static void getLineChartImages(String title, String url, DefaultCategoryDataset dataset, boolean isGroup, boolean numberAxis, int width, int height, CategoryLabelPositions c, RectangleEdge r) {
        JFreeChart chart = getLineChart(title, dataset, numberAxis, isGroup, c, r);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(url);
            ChartUtilities.writeChartAsPNG(fos, chart, width, height);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成时间轴图
     *
     * @param url        保存路径
     * @param dataset    数据集
     * @param isGroup
     * @param numberAxis
     * @param width      图片宽度
     * @param height     图片高度
     */
    public static void getTimerChartImages(String title, String url, TimeSeriesCollection dataset, boolean isGroup, boolean numberAxis, int width, int height, int unit, RectangleEdge r) {
        //JFreeChart chart=getLineChart(title,dataset, numberAxis, isGroup,c,r);
        JFreeChart chart = createChart(title, unit, dataset, r);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(url);
            ChartUtilities.writeChartAsPNG(fos, chart, width, height);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除图片
     *
     * @param imgName
     */
    public static void deleteImg(String imgName) {
        File img = null;
        try {
            img = new File(imgName);
            boolean b = img.exists();
            if (b) {
                img.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 创建主题样式
     *
     * @return
     */
    public static StandardChartTheme getChartTheme() {
        // 创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋体", Font.PLAIN, 20));
        return standardChartTheme;
    }


}
