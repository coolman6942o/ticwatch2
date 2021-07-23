package com.mobvoi.ticwatch.framework.core.responses;

import java.util.List;
import lombok.Data;

/**
 * @Project :
 * @Package Name : com.mobvoi.elearning.vo.web
 * @Description : 分页实体类
 * @Author : zhangbin
 * @Create Date : 2019-06-20 16:55
 * @ModificationHistory Who   When     What
 * ------------    --------------    ---------------------------------
 */
@Data
public class PageDTO<T> {

    private List<T> content;

    private Integer totalElements;
}
