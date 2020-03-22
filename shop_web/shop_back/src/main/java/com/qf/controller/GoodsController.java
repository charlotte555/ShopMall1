package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.entity.Goods;
import com.qf.entity.ResultData;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController {


    //选择一个本地路径路径作为从前台传来的图片存储路径
    private String uploadPath="G:/practice/picture";

    @Value("${ImgPath}")
    private String ImgPath;

    //注入对象:文件存储巴拉巴拉
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Reference
    private IGoodsService goodsService;
    //单表查询
//    //商品列表展示
//    @RequestMapping("list")
//    public String list(Model model){
//        List<Goods> list= goodsService.getGoodsList();
//        model.addAttribute("list",list);
//        return "goodsList";
//    }

    //
    @RequestMapping("getGoodsList")
    public String getGoodsList(Model model){

        List<Goods> list=goodsService.getGoodsLists();
        model.addAttribute("list",list);
        return "goodsList";
    }

    //上传单个封面
    @RequestMapping("/uploader")
    @ResponseBody
    public ResultData<String> uploaderSubject(MultipartFile file){

         String fullPath=null;
        //上传图片到fdfs
        try {
            StorePath path = fastFileStorageClient.uploadImageAndCrtThumbImage(
                    //将前台传来的图片转换成输入流
                    //这个流会自动关闭
                    file.getInputStream(),
                    //传来的图片大小
                    file.getSize(),
                    //文件后缀
                    "jpg",
                    //元数据???
                    null
            );
             //fdfs返回一个上传的路径,路径格式为:
             fullPath = path.getFullPath();

            System.out.println("使用fdfs上传图片拿到的全路径"+path.getFullPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResultData<String>().setCode(ResultData.ResultDataList.okCode).setMsg("上传封面成功").setData(ImgPath+fullPath);
    }
//    @RequestMapping("/uploader")
//    @ResponseBody
//    public ResultData<String> uploaderSubject(MultipartFile file){
//
//        System.out.println("前台传来的文件名"+file.getOriginalFilename());
//        //给图片起个名字
//        String fileName = UUID.randomUUID().toString().replaceAll("-", "")+file.getOriginalFilename();
//
//        //文件存放的路径
//        String path=uploadPath+"/"+fileName;
//        try {
//            //文件调用输入流读取文件
//            InputStream inputStream = file.getInputStream();
//            //再用输出流写入保存到本地;输出流必须写到文件路径的文件名(可以不包括后缀)
//            OutputStream outputStream=new FileOutputStream(path);
//
//            IOUtils.copy(inputStream,outputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(path);
//        //给前台响应图片上传的情况
//        return new ResultData<String>().setCode(ResultData.ResultDataList.okCode).setMsg("上传封面成功").setData(path);
//    }

    /**
     * 图片回显
     *
     */
//    @RequestMapping("/backImg")
//    public void backImg(String imgPath, HttpServletResponse response){
//
////        System.out.println("回显拿到的本地图片存储路径:"+imgPath);
//        try {
//
//            //从本地路径里读取图片
//            InputStream inputStream=new FileInputStream(imgPath);
//            //输出响应给前台
//            ServletOutputStream outputStream = response.getOutputStream();
//            //使用这个copy这个方法将读取到的图片输出到前台
//            IOUtils.copy(inputStream,outputStream);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    //添加商品
    @RequestMapping("/addGoods")
    public String addGoods(Goods goods){

        Integer i=goodsService.addGoods(goods);
        return "redirect:/goods/getGoodsList";
    }

//    //测试ajax的全局异常
//    @RequestMapping("/pic")
//    @ResponseBody
//    public ResultData pic(String name){
//        return new ResultData().setCode(ResultData.ResultDataList.okCode).setMsg("请求成功");
//    }
}
