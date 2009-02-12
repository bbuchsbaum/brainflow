package brainflow.functional;

import brainflow.image.io.ImageInfo;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.FileNotFoundException;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.DataConversionException;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Aug 8, 2004
 * Time: 6:25:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionalDataset {

    Logger log = Logger.getLogger(FunctionalDataset.class.getName());

    private String name;

    private String id;

    private String basepath;

    private String description;

    private List<ImageInfo> imageList = new ArrayList<ImageInfo>();

    private int nimages = 0;

    private int nfiles = 0;


    public FunctionalDataset(String configFile) throws ConfigurationException {
        SAXBuilder parser = new SAXBuilder();
        try {
            Document doc = parser.build(new java.io.FileReader(configFile));

            Element root = doc.getRootElement();

            log.info("Reading Functional Dataset Configuration file " + configFile);
            id = root.getText();
            log.info("Dataset id : " + id);
            name = root.getAttribute("name").getValue();
            log.info("Dataset name : " + name);
            basepath = root.getAttributeValue("basepath");
            log.info("basepath : " + basepath);
            description = root.getAttributeValue("description");
            log.info("description : " + description);

            FactorParser fp = new FactorParser();
            fp.parseFactorStructure(root);

        } catch (JDOMException e) {
            throw new ConfigurationException(e);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException(e);
        } catch(IOException e) {
            throw new ConfigurationException(e);
        }

    }

    class FactorParser {

        Element root;
        Map factorMap = new HashMap();
        Map activeCategories = new HashMap();

        public FactorParser() {
        }

        public void parseFactorStructure(Element parent) throws DataConversionException {

            List factorList = parent.getChildren("factor");
            for (int i = 0; i < factorList.size(); i++) {
                Element element = (Element) factorList.get(i);
                log.info("factor : " + element.getText());
                String level = element.getAttribute("level").getValue();
                openCategory(element.getText(), level);
                if (element.getChildren("factor").size() > 0) {
                    parseFactorStructure(element);
                } else {
                    Element dataframe = element.getChild("dataframe");

                    if (dataframe != null) {
                        readDataFrame(dataframe);
                    }
                }

                closeCategory(element.getText());
            }



        }

        private void readDataFrame(Element dataframe) {
            

        }

        private void openCategory(String name, Object level) {
            Factor factor = (Factor) factorMap.get(name);
            if (factor == null) {
                factor = new Factor(name);
            }

            log.info("opening factor : " + factor.getName());
            log.info("level : " + level);
            factor.addLevel(level.toString());
            factorMap.put(name, factor);
            activeCategories.put(name, level);
        }

        private void closeCategory(String name) {
            activeCategories.remove(name);
        }

    }


    public static void main(String[] args) {
        try {
            FunctionalDataset dset = new FunctionalDataset("c:/laura_ruff.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
