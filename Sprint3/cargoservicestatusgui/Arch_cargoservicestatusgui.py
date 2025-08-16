### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('cargoservicestatusguiArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_gui', graph_attr=nodeattr):
          gui_api_gateway=Custom('gui_api_gateway','./qakicons/symActorWithobjSmall.png')
          gui_state_observer=Custom('gui_state_observer','./qakicons/symActorWithobjSmall.png')
          gui_request_handler=Custom('gui_request_handler','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctx_cargoservice', graph_attr=nodeattr):
          cargoservice=Custom('cargoservice(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='hold_state_update', **evattr, decorate='true', fontcolor='darkgreen') >> gui_state_observer
     gui_request_handler >> Edge(color='magenta', style='solid', decorate='true', label='<loadrequest<font color="darkgreen"> loadaccepted loadrejected</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     gui_api_gateway >> Edge(color='magenta', style='solid', decorate='true', label='<loadrequest<font color="darkgreen"> loadaccepted loadrejected</font> &nbsp; >',  fontcolor='magenta') >> gui_request_handler
     gui_state_observer >> Edge(color='blue', style='solid',  decorate='true', label='<update_hold_json &nbsp; >',  fontcolor='blue') >> gui_state_observer
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<hold_state_update &nbsp; >',  fontcolor='blue') >> gui_state_observer
     gui_request_handler >> Edge(color='blue', style='solid',  decorate='true', label='<update_hold_json &nbsp; >',  fontcolor='blue') >> gui_request_handler
diag
