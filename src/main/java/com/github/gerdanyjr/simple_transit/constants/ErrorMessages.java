package com.github.gerdanyjr.simple_transit.constants;

import java.util.function.Function;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {
        public static final Function<String, String> LOGIN_ALREADY_REGISTERED = (login) -> String
                        .format("Usuário já cadastrado com login: %s", login);

        public static final Function<String, String> USER_NOT_FOUND = (login) -> String
                        .format("Usuário não encontrado com login: %s", login);

        public static final Function<Integer, String> REPORT_NOT_FOUND = (id) -> String
                        .format("Ocorrência não encontrado com id: %s", id);

        public static final Function<Integer, String> REPORT_TYPE_NOT_FOUND = (id) -> String
                        .format("ReportType não encontrado com id: %s", id);

        public static final String FUTURE_REPORT_DATE = "Não é possível cadastrar eventos com uma data futura";

        public static final String REPORT_DATE_TOO_OLD = "Não é possível cadastrar eventos que ocorreram há mais de 2 dias";

        public static final String UNAUTHORIZED = "O usuário não tem permissão para executar esta ação";
}
