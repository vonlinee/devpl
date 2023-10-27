package io.devpl.generator.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestClass {

    @Test
    public void testAddUser() {

    }

    public Response<Model<Info>> prepareObject() {
        Response<Model<Info>> response = new Response<>();

        response.setCode(200);
        response.setMessage("message");

        List<Model<Info>> models = new ArrayList<>();

        for (int i = 0; i < 2; i++) {

            Model<Info> model = new Model<>();
            List<Info> infos = new ArrayList<>();

            for (int j = 0; j < 3; j++) {
                Info info = new Info();
                info.setId(i * j);
                info.setName("Name" + i + j);
                infos.add(info);
            }
            model.setList(infos);

            models.add(model);
        }

        response.setData(models);
        return response;
    }
}
