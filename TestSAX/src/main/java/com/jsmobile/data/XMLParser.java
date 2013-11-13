package com.jsmobile.data;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by wangxin on 10/28/13.
 */
public abstract class XMLParser {
    private SAXParser mParser;
    private XMLReader mReader;

    private Thread mThread;

    public XMLParser() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            mParser = factory.newSAXParser();
            mReader = mParser.getXMLReader();
            mReader.setContentHandler(new CustomContentHandler(this));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void parse(InputSource source){
        try {
            mReader.parse(source);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void parse(InputStream is){
        try {
            mReader.parse(new InputSource(is));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void parseAsync(final InputSource source, final AsyncParseCallback callback){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mReader.parse(source);
                    callback.onParseFinished();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    public static interface AsyncParseCallback{
        public void onParseFinished();
    }

    public void interruptParse(){
        if(mThread != null){
            mThread.interrupt();
        }
    }

    private class CustomException extends SAXException{

    }

    protected void onStartDocument(){

    }

    protected void onEndDocument(){

    }

    protected void onStartElement(String name, Attributes attributes){

    }

    protected void onEndElement(String name){

    }

    protected void onText(String text){

    }

    private class CustomContentHandler extends DefaultHandler{
        private XMLParser mXMLParser;
        public CustomContentHandler(XMLParser xmlParser){
            mXMLParser = xmlParser;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            mXMLParser.onStartDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            mXMLParser.onEndDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            mXMLParser.onStartElement(localName, attributes);

            if(Thread.interrupted()){
                throw new CustomException();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            mXMLParser.onEndElement(localName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String str = new String(ch, start, length);
            if(str.trim().length() > 0){
                mXMLParser.onText(str);
            }
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            super.error(e);
        }
    }
}
