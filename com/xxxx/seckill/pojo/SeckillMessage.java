package com.xxxx.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shkstart
 * @create 2022-09-24 9:53
 */

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SeckillMessage {

    private User user;
    private Long goodId;
}
