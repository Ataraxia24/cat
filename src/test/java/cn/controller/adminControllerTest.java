package cn.controller;

import cn.common.WordFilter;
import org.junit.jupiter.api.Test;

public class adminControllerTest {

    @Test
    public void test() {
        String text = "然后，成人BT";
        String replaceWords = WordFilter.replaceWords(text);
        System.out.println(replaceWords);
    }
}
