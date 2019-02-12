package ru.javawebinar.topjava.util.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatDate extends SimpleTagSupport {
    private String format;

    public void setFormat(String format) {
        this.format = format;
    }

    private StringWriter writer = new StringWriter();

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        getJspBody().invoke(writer);
        if (format == null) {
            format = "dd.MM.yyyy";
        }
        LocalDateTime localDate = LocalDateTime.parse(writer.toString());
        out.print(localDate.format(DateTimeFormatter.ofPattern(format)));
    }
}