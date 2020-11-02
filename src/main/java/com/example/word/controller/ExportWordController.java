package com.example.word.controller;


import com.deepoove.poi.data.PictureRenderData;
import com.example.word.util.ChartUtil;
import com.example.word.util.WordUtil;
import com.example.word.util.WraperDataUtil;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/auth/exportWord/")
@RestController
public class ExportWordController {

    /**
     * 导出word首页
     */
    @RequestMapping(value = "/index")
    public ModelAndView toIndex(HttpServletRequest request) {
        return new ModelAndView("export/index");
    }

    /**
     * 用户信息导出word --- poi-tl
     *
     * @throws IOException
     */
    @RequestMapping("/exportUserWord")
    public void exportUserWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		response.setContentType("application/vnd.ms-excel");  
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".docx");
        Map<String, Object> params = new HashMap<>();
        // 渲染文本
        params.put("name", "张三");
        params.put("position", "开发工程师");
        params.put("entry_time", "2020-07-30");
        params.put("province", "江苏省");
        params.put("city", "南京市");
        // 渲染图片
        String imgPathPre = "F:/temp/img/";
        Map<String, Long> mapPieOrBar = new HashMap<String, Long>();
        mapPieOrBar.put("类型A", 111L);
        mapPieOrBar.put("类型B", 88L);
        mapPieOrBar.put("类型C", 211L);
        DefaultPieDataset dpd = WraperDataUtil.formatPieDpd(mapPieOrBar);
        String imgFileName = imgPathPre + UUID.randomUUID() + ".png";
        // 饼状图
        ChartUtil.getPieChartImage(dpd, imgFileName, 430, 230, RectangleEdge.RIGHT);
        params.put("picture", new PictureRenderData(430, 230, imgFileName));
        // TODO 渲染其他类型的数据请参考官方文档
        // 柱状图
        // 渲染图片
        Map<String, Long> mapPieOrBar1 = new HashMap<String, Long>();
        mapPieOrBar1.put("类型A", 111L);
        mapPieOrBar1.put("类型B", 88L);
        mapPieOrBar1.put("类型C", 211L);
        CategoryDataset dpd1 = WraperDataUtil.formatBarDcd(mapPieOrBar, "2");
        String img2 = imgPathPre + UUID.randomUUID() + ".png";

        ChartUtil.getBarChartImages("柱状图", dpd1, img2, 300, 200, PlotOrientation.VERTICAL, false,
                new Color(255, 255, 0), CategoryLabelPositions.DOWN_45, RectangleEdge.RIGHT);
        params.put("picture1", new PictureRenderData(300, 200, img2));
        //word模板地址放在src/main/webapp/下
        //表示到项目的根目录（webapp）下，要是想到目录下的子文件夹，修改"/"即可
        String path = request.getSession().getServletContext().getRealPath("/");
        String templatePath = path + "template/user.docx";
        WordUtil.downloadWord(response.getOutputStream(), templatePath, params);
    }
}
