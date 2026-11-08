package ua.com.foxminded.task.config.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ua.com.foxminded.task.domain.dto.SubjectDto;

@Component
public class SubjectFormatter implements Formatter<SubjectDto> {

    @Override
    public SubjectDto parse(String id, Locale locale) throws ParseException {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(Integer.parseInt(id));
        return subjectDto;
    }

    @Override
    public String print(SubjectDto subject, Locale locale) {
        return String.valueOf(subject.getId());
    }
}
