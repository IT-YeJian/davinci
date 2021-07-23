package edp.davinci.dto.poiDto;

import lombok.Data;

import java.io.Serializable;

/**
 * POI Excel报表导出，列合并实体<br>
 */
@Data
public class PoiInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String content;

    private String oldContent;

    private int rowIndex;

    private int cellIndex;

    public PoiInfo() {
    }

    public PoiInfo(String content, String oldContent, int rowIndex,
                   int cellIndex) {
        this.content = content;
        this.oldContent = oldContent;
        this.rowIndex = rowIndex;
        this.cellIndex = cellIndex;
    }

    @Override
    public String toString() {
        return "PoiInfo [content=" + content + ", oldContent=" + oldContent
                + ", rowIndex=" + rowIndex + ", cellIndex=" + cellIndex + "]";
    }

}