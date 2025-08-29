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
with Diagram('testobserveArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_update', graph_attr=nodeattr):
          updater=Custom('updater(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_observer', graph_attr=nodeattr):
          observer=Custom('observer','./qakicons/symActorWithobjSmall.png')
     updater >> Edge(color='blue', style='solid',  decorate='true', label='<changed &nbsp; >',  fontcolor='blue') >> observer
diag
