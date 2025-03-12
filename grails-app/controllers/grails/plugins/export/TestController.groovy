package grails.plugins.export

class TestController {
    def exportService

    def index() { 
        def data = [[name:'Grails', version:'5.0']]
        String ext = params.extension ?: params.format
        if (ext in ['csv', 'xls', 'xlsx', 'ods', 'pdf', 'rtf', 'xml']) {
            response.setHeader("Content-disposition", "attachment; filename=test.${ext}")
            exportService.export(ext == 'xls' || ext == 'xlsx' ? 'excel' : ext, response.outputStream, data, [:], [ 'fileFormat': ext])
            return
        }

        respond data
    }
}
