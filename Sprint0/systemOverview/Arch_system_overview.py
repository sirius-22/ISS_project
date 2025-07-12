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
with Diagram('system_overviewArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_cargoservice', graph_attr=nodeattr):
          cargoservicestatusgui=Custom('cargoservicestatusgui','./qakicons/symActorWithobjSmall.png')
          slotmanagement=Custom('slotmanagement','./qakicons/symActorWithobjSmall.png')
          cargorobot=Custom('cargorobot','./qakicons/symActorWithobjSmall.png')
          cargoservice=Custom('cargoservice','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctx_productservice', graph_attr=nodeattr):
          productservice=Custom('productservice(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_raspdevice', graph_attr=nodeattr):
          sonardevice=Custom('sonardevice','./qakicons/symActorWithobjSmall.png')
          leddevice=Custom('leddevice','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctx_client_simulator', graph_attr=nodeattr):
          client_simulator=Custom('client_simulator','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     sonardevice >> Edge( label='distance', **eventedgeattr, decorate='true', fontcolor='red') >> cargoservice
     cargorobot >> Edge(color='magenta', style='solid', decorate='true', label='<step &nbsp; >',  fontcolor='magenta') >> basicrobot
     client_simulator >> Edge(color='magenta', style='solid', decorate='true', label='<loadrequest<font color="darkgreen"> loadaccepted loadrejected</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     client_simulator >> Edge(color='magenta', style='solid', decorate='true', label='<registrationrequest<font color="darkgreen"> registrationaccepted</font> &nbsp; >',  fontcolor='magenta') >> productservice
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<productdatareq &nbsp; >',  fontcolor='magenta') >> productservice
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> cargoservicestatusgui
     cargorobot >> Edge(color='blue', style='solid',  decorate='true', label='<cmd &nbsp; >',  fontcolor='blue') >> basicrobot
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<updateled &nbsp; >',  fontcolor='blue') >> leddevice
     cargorobot >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> cargoservicestatusgui
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<movecontainer &nbsp; stop &nbsp; resume &nbsp; >',  fontcolor='blue') >> cargorobot
diag
