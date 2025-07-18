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
with Diagram('logic_modelArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_cargoservice', graph_attr=nodeattr):
          sonar_mock=Custom('sonar_mock','./qakicons/symActorWithobjSmall.png')
          slotmanagement_mock=Custom('slotmanagement_mock','./qakicons/symActorWithobjSmall.png')
          cargorobot=Custom('cargorobot','./qakicons/symActorWithobjSmall.png')
          cargoservice=Custom('cargoservice','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctx_productservice', graph_attr=nodeattr):
          productservice=Custom('productservice(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_client_simulator', graph_attr=nodeattr):
          client_simulator=Custom('client_simulator','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     sonar_mock >> Edge( label='resumeActions', **eventedgeattr, decorate='true', fontcolor='red') >> cargorobot
     sonar_mock >> Edge( label='stopActions', **eventedgeattr, decorate='true', fontcolor='red') >> cargorobot
     cargorobot >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='resumeActions', **evattr, decorate='true', fontcolor='darkgreen') >> cargorobot
     sonar_mock >> Edge( label='resumeActions', **eventedgeattr, decorate='true', fontcolor='red') >> cargoservice
     sonar_mock >> Edge( label='stopActions', **eventedgeattr, decorate='true', fontcolor='red') >> cargoservice
     sonar_mock >> Edge( label='containerhere', **eventedgeattr, decorate='true', fontcolor='red') >> cargoservice
     sys >> Edge( label='resumeActions', **evattr, decorate='true', fontcolor='darkgreen') >> cargoservice
     cargorobot >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     client_simulator >> Edge(color='magenta', style='solid', decorate='true', label='<loadrequest<font color="darkgreen"> loadaccepted loadrejected</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     client_simulator >> Edge(color='magenta', style='solid', decorate='true', label='<registrationrequest<font color="darkgreen"> registrationaccepted</font> &nbsp; >',  fontcolor='magenta') >> productservice
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<productdatareq<font color="darkgreen"> productdata errorproductdata</font> &nbsp; >',  fontcolor='magenta') >> productservice
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<freeSlot<font color="darkgreen"> slotname</font> &nbsp; totalWeightReq<font color="darkgreen"> totalWeight</font> &nbsp; >',  fontcolor='magenta') >> slotmanagement_mock
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<loadcontainer<font color="darkgreen"> containerloaded</font> &nbsp; >',  fontcolor='magenta') >> cargorobot
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<updatedatahold &nbsp; >',  fontcolor='blue') >> slotmanagement_mock
diag
