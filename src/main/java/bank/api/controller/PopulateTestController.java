package bank.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/populate")
@Tag(name = "0 - Populate", description = "Popular Banco de Dados (Opcional)")
public class PopulateTestController {
    private final DataSource dataSource;

    public PopulateTestController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping
    public String populate() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("inserts-bank-api.sql"));
            return "Banco de dados populado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao popular o banco de dados: " + e.getMessage();
        }
    }
}