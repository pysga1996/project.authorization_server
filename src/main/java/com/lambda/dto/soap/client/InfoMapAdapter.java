package com.lambda.dto.soap.client;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

public class InfoMapAdapter extends XmlAdapter<InfoMapDTO, Map<String, Object>> {

    @Override
    public Map<String, Object> unmarshal(InfoMapDTO v) throws Exception {
        Map<String, Object> infoMap = new HashMap<>();
        for (InfoMapDTO.Entry entry: v.entry) {
            infoMap.put(entry.key, entry.value);
        }
        return infoMap;
    }

    @Override
    public InfoMapDTO marshal(Map<String, Object> v) throws Exception {
        InfoMapDTO infoMap = new InfoMapDTO();
        for (Map.Entry<String, Object> entry: v.entrySet()) {
            InfoMapDTO.Entry e = new InfoMapDTO.Entry();
            e.setKey(entry.getKey());
            e.setValue(entry.getValue());
            infoMap.getEntry().add(e);
        }
        return infoMap;
    }
}
