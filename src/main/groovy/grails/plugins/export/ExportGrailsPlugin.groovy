package grails.plugins.export

import grails.plugins.Plugin
import groovy.util.logging.Commons
import org.apache.log4j.Logger
import org.slf4j.LoggerFactory

@Commons
class ExportGrailsPlugin extends Plugin {
    // the plugin version
    def version = "2.0-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    
    def title = "Export Plugin" // Headline display name of the plugin

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/export"

    def author = "Grails Plugin Collective"
    def authorEmail = "grails.plugin.collective@gmail.com"
    def description = '''\
This plugin offers export functionality supporting different formats e.g. CSV, Excel, Open Document Spreadsheet, PDF and XML 
and can be extended to add additional formats. 
'''
    def license = 'APACHE'
    def organization = [name: 'Grails Plugin Collective', url: 'http://github.com/gpc']
    def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPEXPORT']
    def scm = [url: 'https://github.com/gpc/grails-export']



    Closure doWithSpring() { { ->
			//This is only necessary here, because later on log is injected by Spring
		def log = LoggerFactory.getLogger(ExportGrailsPlugin.class)
		 
		"exporterFactory"(grails.plugins.export.exporter.DefaultExporterFactory)
		
		try {				
			grailsApplication.config.exporters.each { key, value ->
		  		try {
		  			//Override default renderer configuration
					if(grailsApplication.config?.export."${key}"){
						value = grailsApplication.config.export."${key}"
					}
		  			
		      		Class clazz = Class.forName(value, true, new GroovyClassLoader())
		      		
		      		//Add to spring
		      		"$key"(clazz) { bean ->
						  bean.scope = "prototype"
					}	
		  		} catch(ClassNotFoundException e){
		  			log.error("Couldn't find class: ${value}", e)
				}
			}
		} catch(Exception e){
			log.error("Error initializing Export plugin", e)
		}
		catch(Error e){
			//Strange error which happens when using generate-all and hibernate.cfg
			log.error("Error initializing Export plugin")
		}
    } }

}
