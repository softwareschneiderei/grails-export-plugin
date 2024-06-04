package grails.plugins.export

class TestController {
    def exportService

    def index() { 
        def list = [[name:'Grails', version:'5.0']]
        String ext = params.extension?:params.format
        if (ext in ['csv', 'xls', 'ods', 'pdf', 'rtf', 'xml']) {
            response.setHeader("Content-disposition", "attachment; filename=test.${ext}")
            exportService.export(ext == 'xls'?'excel':ext, response.outputStream, list, [:], [:])
        } else {
            respond list
        }
    }
}
