package com.cowave.sys.meter.domain.torna.param;

import com.gitee.easyopen.doc.annotation.ApiDocField;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * @author tanghc
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DocPushItemParam {

    @Length(max = 64, message = "文档名称长度不能超过64")
    @NotBlank(message = "文档名称不能为空")
    @ApiDocField(description = "文档名称", required = true)
    private String name;

    @ApiDocField(description = "文档概述")
    private String description;

    @ApiDocField(description = "接口维护人")
    private String author;

    @Length(max = 128, message = "'deprecated' 长度不能超过128")
    @ApiDocField(description = "废弃描述，有值代表废弃")
    private String deprecated;

    @ApiDocField(description = "0:http,1:dubbo,2:富文本,3:markdown")
    private Byte type;

    @Length(max = 200, message = "'url' 长度不能超过200")
    @ApiDocField(description = "请求url")
    private String url;

    @Length(max = 32, message = "'version' 长度不能超过32")
    @ApiDocField(description = "版本号，默认为空字符串")
    private String version = "";

    @Length(max = 200, message = "'definition' 长度不能超过200")
    @ApiDocField(description = "dubbo的接口定义")
    private String definition;

    @Length(max = 12, message = "'httpMethod' 长度不能超过12")
    @ApiDocField(description = "http方法")
    private String httpMethod;

    @Length(max = 128, message = "'contentType' 长度不能超过128")
    @ApiDocField(description = "contentType")
    private String contentType;

    @ApiDocField(description = "是否是文件夹，1：是，0：否")
    private Byte isFolder;

    @ApiDocField(description = "是否显示，1：显示，0：不显示")
    private Byte isShow;

    @ApiDocField(description = "排序")
    private Integer orderIndex;

    @ApiDocField(description = "是否请求数组")
    private Byte isRequestArray = 0;

    @ApiDocField(description = "是否返回数组")
    private Byte isResponseArray = 0;

    @Builder.Default
    @ApiDocField(description = "请求数组时元素类型, object/number/string/boolean")
    private String requestArrayType = "object";

    @Builder.Default
    @ApiDocField(description = "返回数组时元素类型, object/number/string/boolean")
    private String responseArrayType = "object";

    @ApiDocField(description = "dubbo服务信息")
    private DubboParam dubboInfo;

    @ApiDocField(description = "path参数", elementClass = DocParamPushParam.class)
    private List<DocParamPushParam> pathParams;

    @ApiDocField(description = "请求头", elementClass = HeaderParamPushParam.class)
    private List<HeaderParamPushParam> headerParams;

    @ApiDocField(description = "Query参数", elementClass = DocParamPushParam.class)
    private List<DocParamPushParam> queryParams;

    @ApiDocField(description = "Body参数", elementClass = DocParamPushParam.class)
    private List<DocParamPushParam> requestParams;

    @ApiDocField(description = "返回参数", elementClass = DocParamPushParam.class)
    private List<DocParamPushParam> responseParams;

    @ApiDocField(description = "错误码", elementClass = CodeParamPushParam.class)
    private List<CodeParamPushParam> errorCodeParams;

    @ApiDocField(description = "文档项")
    private List<DocPushItemParam> items;

    private String tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocPushItemParam that = (DocPushItemParam) o;
        return name.equals(that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(httpMethod, that.httpMethod) &&
                Objects.equals(isFolder, that.isFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, httpMethod, isFolder);
    }

}
