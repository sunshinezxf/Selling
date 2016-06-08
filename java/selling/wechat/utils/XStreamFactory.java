package selling.wechat.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Writer;

/**
 * Created by sunshine on 5/25/16.
 */
public class XStreamFactory {
    protected static String PREFIX_CDATA = "<![CDATA[";
    protected static String SUFFIX_CDATA = "]]>";

    public XStreamFactory() {
    }

    public static XStream init(boolean isAddCDATA) {
        XStream xstream = null;
        if (isAddCDATA) {
            xstream = new XStream(new XppDriver() {
                public HierarchicalStreamWriter createWriter(final Writer out) {
                    return new PrettyPrintWriter(out) {
                        protected void writeText(QuickWriter writer, String text) {
                            if (!text.startsWith(XStreamFactory.PREFIX_CDATA)) {
                                text = XStreamFactory.PREFIX_CDATA + text + XStreamFactory.SUFFIX_CDATA;
                            }

                            writer.write(text);
                        }
                    };
                }
            });
        } else {
            xstream = new XStream();
        }

        return xstream;
    }
}