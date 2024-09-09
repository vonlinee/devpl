package io.devpl.codegen;

public class JHTool {

    static final String table = """
        支出类型	categoryCode
        费用承担对象	objectName
        发票文件	invoiceLink
        发票状态	invVerifySta
        检验结果	invVerifyInf
        发票号码	invoiceNo
        发票日期	invoiceDate
        发票类型	invoiceType
        发票金额	totalInvAmt
        """;

    public static void main(String[] args) {

        String[] split = table.split("\n");

        StringBuilder sb = new StringBuilder();

        for (String s : split) {
            String[] split1 = s.split("\t");

            sb.append("@ApiModelProperty(value = \"").append(split1[0]).append("\")\n\t");

            sb.append("private String ").append(split1[1]).append(";\t");
        }

        System.out.println(sb);
    }
}
