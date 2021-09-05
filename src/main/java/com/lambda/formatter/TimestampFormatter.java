package com.lambda.formatter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TimestampFormatter implements Formatter<Timestamp> {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @Override
    @NonNull
    public Timestamp parse(@NonNull String text, @NonNull Locale locale) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return Timestamp.from(Instant.ofEpochMilli(sdf.parse(text).getTime()));
        } catch (ParseException ex) {
            return Timestamp.from(Instant.now());
        }
    }

    @Override
    @NonNull
    public String print(@NonNull Timestamp object, @NonNull Locale locale) {
        return this.simpleDateFormat.format(new Date(object.getTime()));
    }
}
