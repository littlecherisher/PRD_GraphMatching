/**
 * Created by kevmi on 26/01/2017. 
 * Modified by Kai on 24/02/2018.
 */

function info(id, iflag) {
	$.get('/GraphMatching/DetailAppareillement?execution=' + id + '&methode='
			+ $("#methode").val() + '&G1=' + $("#G1").val() + '&G2='
			+ $("#G2").val(), function(responseText) {
		document.getElementById('resultat').innerHTML = responseText;
		$('.noeuds').dataTable({
			"bFilter" : true,
			"sDom" : '<ti>',
			"scrollY" : "400px",
			"scrollCollapse" : true,
			"paging" : false
		}

		);
		$('.arcs').dataTable({
			"bFilter" : true,
			"sDom" : '<ti>',
			"scrollY" : "400px",
			"scrollCollapse" : true,
			"paging" : false
		});
	});
	iflag++;
	return iflag;
}

function getGraphesData() {
	//Partie 1：Lisez les données de la table de pages et générez un tableau bidimensionnel data_VC0 (pour la correspondance à double image) et getGraphNum.
	{
		var data_VC0 = [];
		var tb = document.getElementById("noeuds");
		var rows = tb.rows;
		var data_VC0 = [];
		var graphNum = [];
		for (var i = 0; i < rows.length; i++) {
			data_VC0[i] = [];
			var cells = rows[i].cells;
			for (var j = 0; j < cells.length; j++) {
				if (i == 0) {
					var getGraphNum = cells[j].innerHTML;
					var x = getGraphNum.indexOf('molecule_');
					var y = getGraphNum.indexOf('</div>');
					graphNum[j] = getGraphNum.substring(x, y);
					// alert(graphNum[j]);
				} else {
					data_VC0[i - 1][j] = cells[j].innerHTML;
					// alert(data_VC0[i-1][j]);
				}
			}
		}
	}
	//Partie 2: Lisez le fichier .dat et effectuez une division des données pour générer un tableau tridimensionnel de GraphsData_V_V et Graphs_Data_E_E
	{
		var getGraphesData = document.getElementById('getGraphesData').value;
		var cutGraphesData = getGraphesData.split("molecule_");
		var GraphesData = [];
		for (var i = 1; i < cutGraphesData.length; i++) {
			cutGraphesData[i] = "molecule_" + cutGraphesData[i];
			GraphesData[i - 1] = cutGraphesData[i];
			// alert(GraphesData[i-1]);
		}
		var GraphesData_Name = [];
		var GraphesData_VE = [];
		var GraphesData_V = [];
		var GraphesData_E = [];
		var cutGraphesData_V = [];
		var cutGraphesData_E = [];
		var GraphesData_V_F = [];
		var GraphesData_E_F = [];
		var GraphesData_V_Z = [];
		var GraphesData_E_Z = [];
		var GraphesData_V_V = [];
		var GraphesData_E_E = [];
		for (var i = 0; i < GraphesData.length; i++) {
			cutGraphesData_V[i] = [];
			cutGraphesData_E[i] = [];
			GraphesData_V_F[i] = [];
			GraphesData_E_F[i] = [];
			GraphesData_V_Z[i] = [];
			GraphesData_E_Z[i] = [];
			GraphesData_V_V[i] = [];
			GraphesData_E_E[i] = [];
			x = GraphesData[i].indexOf('molecule_');
			y = GraphesData[i].indexOf('undirected');
			GraphesData_Name[i] = GraphesData[i].substring(x, y - 1)
			//alert(GraphesData_Name[i]);
			x = GraphesData[i].indexOf('valence:int,');
			GraphesData_VE[i] = GraphesData[i].substring(x + 12);
			x = GraphesData_VE[i].indexOf('E,');
			GraphesData_V[i] = GraphesData_VE[i].substring(0, x);
			GraphesData_E[i] = GraphesData_VE[i].substring(x);
			cutGraphesData_V[i] = GraphesData_V[i].split('V,');
			cutGraphesData_E[i] = GraphesData_E[i].split('E,');
			for (var j = 0; j < cutGraphesData_V[i].length - 1; j++) {
				GraphesData_V_V[i][j] = [];
				GraphesData_V_F[i][j] = cutGraphesData_V[i][j + 1];
				GraphesData_V_Z[i][j] = GraphesData_V_F[i][j].split(',')
				GraphesData_V_V[i][j][0] = GraphesData_V_Z[i][j][0];
				x = GraphesData_V_Z[i][j][2].indexOf('chem:');
				GraphesData_V_V[i][j][1] = GraphesData_V_Z[i][j][2]
						.substring(x + 5);
			}
			for (j = 0; j < cutGraphesData_E[i].length - 1; j++) {
				GraphesData_E_E[i][j] = [];
				GraphesData_E_F[i][j] = cutGraphesData_E[i][j + 1];
				GraphesData_E_Z[i][j] = GraphesData_E_F[i][j].split(',')
				GraphesData_E_E[i][j][0] = GraphesData_E_Z[i][j][0];
				GraphesData_E_E[i][j][1] = GraphesData_E_Z[i][j][1];
				x = GraphesData_E_Z[i][j][3].indexOf('valence:');
				GraphesData_E_E[i][j][2] = GraphesData_E_Z[i][j][3]
						.substring(x + 8);
			}
		}
	}
	//Partie 3: Requête de données pour générer un tableau bidimensionnel de deux images sélectionnées par l'utilisateur actuel (data_V1,data_V2,data_E1,data_E2)
	{
		var moleculeNo = [];
		for (i = 0; i < GraphesData_Name.length; i++) {
			if (graphNum[0] == GraphesData_Name[i])
				moleculeNo[0] = i;
			if (graphNum[1] == GraphesData_Name[i])
				moleculeNo[1] = i;
		}
		var data_V1 = [];
		var data_V2 = [];
		var data_E1 = [];
		var data_E2 = [];
		for (i = 0; i < GraphesData_V_V[moleculeNo[0]].length; i++) {
			data_V1[i] = [];
			data_V1[i][0] = GraphesData_V_V[moleculeNo[0]][i][0];
			data_V1[i][1] = GraphesData_V_V[moleculeNo[0]][i][1];
		}
		/*
		for (i=0;data_V1.length;i++)
			alert("nodename:"+data_V1[i][0]+"\n"+"chemname:"+data_V1[i][1]);
		 */
		for (i = 0; i < GraphesData_V_V[moleculeNo[1]].length; i++) {
			data_V2[i] = [];
			data_V2[i][0] = GraphesData_V_V[moleculeNo[1]][i][0];
			data_V2[i][1] = GraphesData_V_V[moleculeNo[1]][i][1];
		}
		for (i = 0; i < GraphesData_E_E[moleculeNo[0]].length; i++) {
			data_E1[i] = [];
			data_E1[i][0] = GraphesData_E_E[moleculeNo[0]][i][0];
			data_E1[i][1] = GraphesData_E_E[moleculeNo[0]][i][1];
			data_E1[i][2] = GraphesData_E_E[moleculeNo[0]][i][2];
		}
		/*
		for (i=0;data_E1.length;i++)
			alert("from:"+data_E1[i][0]+"\n"+"to:"+data_E1[i][1]+"\n"+"valence:"+data_E1[i][2]);
		 */
		for (i = 0; i < GraphesData_E_E[moleculeNo[1]].length; i++) {
			data_E2[i] = [];
			data_E2[i][0] = GraphesData_E_E[moleculeNo[1]][i][0];
			data_E2[i][1] = GraphesData_E_E[moleculeNo[1]][i][1];
			data_E2[i][2] = GraphesData_E_E[moleculeNo[1]][i][2];
		}
	}
	//Partie 4：Pré-traitement des données, améliorez la disponibilité des données grâce à la conversion de format, générez des données tridimensionnelles data_V et data_E
	{
		var data_V = [];
		var data_E = [];
		var graphnum = graphNum.length;
		for (var k = 0; k < graphnum; k++) {
			var data_V_add = '';
			var data_V_length;
			var data_E_length;
			data_V[k] = [];
			data_E[k] = [];
			var data_V_con = [];
			var data_E_con = [];
			switch (k) {
			case 0:
				data_V_length = data_V1.length;
				data_E_length = data_E1.length;
				data_V_add = 'a';
				for (var s = 0; s < data_V_length; s++) {
					data_V_con[s] = [];
					data_V_con[s][0] = data_V1[s][0];
					data_V_con[s][1] = data_V1[s][1];
				}
				for (s = 0; s < data_E_length; s++) {
					data_E_con[s] = [];
					data_E_con[s][0] = data_E1[s][0];
					data_E_con[s][1] = data_E1[s][1];
					data_E_con[s][2] = data_E1[s][2];
				}
				break;
			case 1:
				data_V_length = data_V2.length;
				data_E_length = data_E2.length;
				data_V_add = 'b';
				for (s = 0; s < data_V_length; s++) {
					data_V_con[s] = [];
					data_V_con[s][0] = data_V2[s][0];
					data_V_con[s][1] = data_V2[s][1];
				}
				for (s = 0; s < data_E_length; s++) {
					data_E_con[s] = [];
					data_E_con[s][0] = data_E2[s][0];
					data_E_con[s][1] = data_E2[s][1];
					data_E_con[s][2] = data_E2[s][2];
				}
				break;
			default:
				alert("Le nombre d'images par défaut est 2, veuillez ajouter un case.");
			}
			for (var v = 0; v < data_V_length; v++) {
				data_V[k][v] = [];
				data_V[k][v][0] = data_V_add + data_V_con[v][0].toString();
				data_V[k][v][1] = data_V_con[v][1].toString();
			}
			for (var e = 0; e < data_E_length; e++) {
				data_E[k][e] = [];
				data_E[k][e][0] = data_V_add + data_E_con[e][0].toString();
				data_E[k][e][1] = data_V_add + data_E_con[e][1].toString();
				data_E[k][e][2] = Number(data_E_con[e][2]);
			}
		}
		var data_VC = [];
		for (var c = 0; c < data_VC0.length - 1; c++) {
			data_VC[c] = [];
			data_VC[c][0] = data_VC0[c][0];
			data_VC[c][1] = data_VC0[c][1];
		}
		for (var c = 0; c < data_VC.length; c++) {
			data_VC[c][0] = 'a' + data_VC[c][0].toString();
			data_VC[c][1] = 'b' + data_VC[c][1].toString();
		}
	}
	//Partie 5
	/*
	Visualisation - tracez les deux images séparément et montrez la connexion des points des deux graphiques dans le graphique total.
	Optimisation du code - Utilise des tableaux tridimensionnels et des fonctions pour parcourir le code pertinent, éliminant environ 750 lignes de code non-pratique, améliorant ainsi l'applicabilité du code.
	Amélioration du système dynamique - force centripète de 0.2, simulation de double coeur, poids de marge variable, répulsion de point variable.
	*/
	{
		for (var justpre = 0; justpre < 3; justpre++) {
			var k;
			var flag;
			if (justpre == 0) {
				k = 0;
				flag = 0;
			} else if (justpre == 1) {
				k = 1;
				flag = 0;

			} else {
				k = 0;
				flag = 1;
			}
			//alert("k="+k+"\n"+"flag="+flag);
			if (flag == 0) {//Configuration du paramètre du système de gravité
				switch (k) {
				case 0:
					var graph_cplex1 = echarts.init(document
							.getElementById('graph_cplex1'));
					var data_V_length = data_V1.length;
					var graphName = graphNum[0];
					break;
				case 1:
					var graph_cplex2 = echarts.init(document
							.getElementById('graph_cplex2'));
					data_V_length = data_V2.length;
					graphName = graphNum[1];
					break;
				default:
					alert("Le nombre d'images par défaut est 2, veuillez ajouter un case.");
				}
				var symbolpre = 1.1;
				var symbloX = null;
				var symbloY = null;
				var repulsion = 5000 / data_V_length;
				var edgeLength = 40;
			} else {
				var graph_cplex = echarts.init(document
						.getElementById('graph_cplex'));
				graphName = "La visualisation de GrapheMatching";
				symbolpre = 1.5;
				graphpre = document.getElementById('graph_cplex');
				symbloX = graphpre.offsetWidth * k;
				symbloY = graphpre.offsetHeight / 2;
				repulsion = 10000 / (data_V1.length + data_V2.length);
				edgeLength = 800;
			}
			var data_c = [];
			var link_c = [];
			for (var graphNo = 0; graphNo <= flag; graphNo++) {
				var kk = k + graphNo;
				if (graphNo == 1) {
					graphpre = document.getElementById('graph_cplex');
					symbloX = graphpre.offsetWidth * kk;
				}
				for (var i = 0; i < data_V[kk].length; i++) {//dessiner les nœuds
					var symbolsize = 12;
					var symbolcolor = '#808080';
					var labelcolor = '#FFFFFF';
					switch (data_V[kk][i][1]) {
					case 'H':
						symbolsize = 1;
						symbolvalue = 1;
						symbolcolor = '#0096FF';
						labelcolor = '#000000';
						break;
					case 'C':
						symbolsize = 12;
						symbolvalue = 12;
						symbolcolor = '#FF9600';
						labelcolor = '#000000';
						break;
					case 'N':
						symbolsize = 14;
						symbolvalue = 5;
						symbolcolor = '#FF00FF';
						labelcolor = '#000000';
						break;
					case 'O':
						symbolsize = 16;
						symbolvalue = 5;
						symbolcolor = '#FF0000';
						labelcolor = '#FFFFFF';
						break;
					case 'Si':
						symbolsize = 18;
						symbolvalue = 6;
						symbolcolor = '#007F00';
						labelcolor = '#FFFFFF';
						break;
					case 'P':
						symbolsize = 21;
						symbolvalue = 6;
						symbolcolor = '#FF007F';
						labelcolor = '#000000';
						break;
					case 'S':
						symbolsize = 22;
						symbolvalue = 6;
						symbolcolor = '#00FF00';
						labelcolor = '#000000';
						break;
					default:
						symbolsize = 12;
						symbolvalue = 6;
						symbolcolor = '#808080';
						labelcolor = '#FFFFFF';
					}
					symbolsize = symbolsize * symbolpre + 10;
					symbolvalue = symbolvalue * symbolpre + 10;
					data_c.push({
						name : data_V[kk][i][0],
						showChem : data_V[kk][i][1],
						value : symbolvalue,
						symbol : 'circle',
						symbolSize : symbolsize,
						x : symbloX,
						y : symbloY,
						itemStyle : {
							normal : {
								color : symbolcolor
							}
						},
						label : {
							emphasis : {
								show : false
							}
						}
					});
				}
				for (var j = 0; j < data_E[kk].length; j++) {//dessiner les arcs
					var lineWidth = 3;
					lineValue = 300;
					if (data_E[kk][j][2] != 2) {
						curveness = 0;
						if (data_E[kk][j][2] != 1)
							lineWidth = 7;
					} else {
						curveness = 0.1;
						lineValue = 295;
					}
					link_c.push({
						source : data_E[kk][j][0],
						target : data_E[kk][j][1],
						value : lineValue,
						lineStyle : {
							normal : {
								color : '#000000',
								width : lineWidth,
								type : 'solid',
								curveness : curveness,
								opacity : 0.8
							}
						},
						symbol : [ 'arrow', 'arrow' ],
						symbolSize : [ 0, 0 ]
					});
					if (data_E[kk][j][2] == 2) {
						link_c.push({
							source : data_E[kk][j][1],
							target : data_E[kk][j][0],
							value : lineValue,
							lineStyle : {
								normal : {
									color : '#000000',
									width : lineWidth,
									type : 'solid',
									curveness : curveness,
									opacity : 0.8
								}
							},
							symbol : [ 'arrow', 'arrow' ],
							symbolSize : [ 0, 0 ]
						})
					}
				}
			}
			if (flag == 1) {//dessiner des lignes entre deux graphes
				for (c = 0; c < data_VC.length; c++) {
					link_c.push({
						source : data_VC[c][0],
						target : data_VC[c][1],
						value : 1,
						lineStyle : {
							normal : {
								color : '#000000',
								width : 1,
								type : 'dashed',
								curveness : 0,
								opacity : 0.5
							}
						},
						symbol : [ 'arrow', 'arrow' ],
						symbolSize : [ 0, 0 ]
					});
				}
			}
			var option = {
				title : {
					text : graphName
				},
				tooltip : {},
				backgroundColor : '#FFFFFF',
				toolbox : {
					show : true,
					feature : {
						restore : {
							show : true
						},
						saveAsImage : {
							show : true
						}
					}
				},
				series : [ {
					type : 'graph',
					tooltip : {
						formatter : function(node) {
							if (!node.data.name) {
								return null;
							} else {
								return node.data.showChem + '('
										+ node.data.name + ')';
							}
						}
					},
					layout : 'force',
					force : {
						layoutAnimation : true,
						gravity : 0.2,
						repulsion : repulsion,
						edgeLength : [ 1, edgeLength ]
					},
					coordinateSystem : null,
					ribbonType : true,
					roam : true,
					nodeScaleRatio : 1,
					draggable : true,
					focusNodeAdjacency : true,
					cursor : 'pointer',
					edgeSymbol : [ 'circle', 'circle' ],
					edgeSymbolSize : [ 0, 0 ],
					data : data_c,
					links : link_c
				} ]
			};
			if (flag == 0) {
				switch (k) {
				case 0:
					graph_cplex1.setOption(option);
					break;
				case 1:
					graph_cplex2.setOption(option);
					break;
				default:
					alert("Le nombre d'images par défaut est 2, veuillez ajouter un case.");
				}
			} else {
				graph_cplex.setOption(option);
			}
		}
	}
}

function getSimpleData() {
	//Partie 6: Lire les données de la table de pages, générer un tableau bidimensionnel data_VC0 (pour une connexion à double image) et getGraphNum
	{
		var data_VC0 = [];
		var tb = document.getElementById("noeuds");
		var rows = tb.rows;
		var data_VC0 = [];
		var graphNum = [];
		for (var i = 0; i < rows.length; i++) {
			data_VC0[i] = [];
			var cells = rows[i].cells;
			for (var j = 0; j < cells.length; j++) {
				if (i == 0) {
					var getGraphNum = cells[j].innerHTML;
					var x = getGraphNum.indexOf('sampletest');
					var y = getGraphNum.indexOf('</div>');
					graphNum[j] = getGraphNum.substring(x, y);
					//alert(graphNum[j]);
				} else {
					data_VC0[i - 1][j] = cells[j].innerHTML;
					//alert(data_VC0[i-1][j]);
				}
			}
		}
	}
	//Partie 7: Lisez le fichier .dat et effectuez une division des données pour générer un tableau tridimensionnel de GraphsData_V_V et Graphs_Data_E_E
	{
		var getGraphesData = document.getElementById('getGraphesData').value;
		var cutGraphesData = getGraphesData.split("sampletest");
		var GraphesData = [];
		for (var i = 1; i < cutGraphesData.length; i++) {
			cutGraphesData[i] = "sampletest" + cutGraphesData[i];
			GraphesData[i - 1] = cutGraphesData[i];
			// alert(GraphesData[i-1]);
		}
		var GraphesData_Name = [];
		var GraphesData_VE = [];
		var GraphesData_V = [];
		var GraphesData_E = [];
		var cutGraphesData_V = [];
		var cutGraphesData_E = [];
		var GraphesData_V_F = [];
		var GraphesData_E_F = [];
		var GraphesData_V_Z = [];
		var GraphesData_E_Z = [];
		var GraphesData_V_V = [];
		var GraphesData_E_E = [];
		for (var i = 0; i < GraphesData.length; i++) {
			cutGraphesData_V[i] = [];
			cutGraphesData_E[i] = [];
			GraphesData_V_F[i] = [];
			GraphesData_E_F[i] = [];
			GraphesData_V_Z[i] = [];
			GraphesData_E_Z[i] = [];
			GraphesData_V_V[i] = [];
			GraphesData_E_E[i] = [];
			x = GraphesData[i].indexOf('sampletest');
			y = GraphesData[i].indexOf('undirected');
			GraphesData_Name[i] = GraphesData[i].substring(x, y - 1)
			//alert(GraphesData_Name[i]);
			x = GraphesData[i].indexOf('angle0:String,');
			GraphesData_VE[i] = GraphesData[i].substring(x + 14);
			x = GraphesData_VE[i].indexOf('E,');
			GraphesData_V[i] = GraphesData_VE[i].substring(0, x);
			GraphesData_E[i] = GraphesData_VE[i].substring(x);
			cutGraphesData_V[i] = GraphesData_V[i].split('V,');
			cutGraphesData_E[i] = GraphesData_E[i].split('E,');
			for (var j = 0; j < cutGraphesData_V[i].length - 1; j++) {
				GraphesData_V_V[i][j] = [];
				GraphesData_V_F[i][j] = cutGraphesData_V[i][j + 1];
				GraphesData_V_Z[i][j] = GraphesData_V_F[i][j].split(',')
				GraphesData_V_V[i][j][0] = GraphesData_V_Z[i][j][0];
				x = GraphesData_V_Z[i][j][2].indexOf('x:');
				GraphesData_V_V[i][j][1] = GraphesData_V_Z[i][j][2]
						.substring(x + 2);
				y = GraphesData_V_Z[i][j][3].indexOf('y:');
				GraphesData_V_V[i][j][2] = GraphesData_V_Z[i][j][3]
						.substring(x + 2);
			}
			for (j = 0; j < cutGraphesData_E[i].length - 1; j++) {
				GraphesData_E_E[i][j] = [];
				GraphesData_E_F[i][j] = cutGraphesData_E[i][j + 1];
				GraphesData_E_Z[i][j] = GraphesData_E_F[i][j].split(',')
				GraphesData_E_E[i][j][0] = GraphesData_E_Z[i][j][0];
				GraphesData_E_E[i][j][1] = GraphesData_E_Z[i][j][1];
				x = GraphesData_E_Z[i][j][3].indexOf('frequency:');
				GraphesData_E_E[i][j][2] = GraphesData_E_Z[i][j][3]
						.substring(x + 10);
				y = GraphesData_E_Z[i][j][3].indexOf('angle0:');
				GraphesData_E_E[i][j][3] = GraphesData_E_Z[i][j][5]
						.substring(x + 7);
				if (GraphesData_E_E[i][j][2] > 1) {
					y = GraphesData_E_Z[i][j][3].indexOf('angle1:');
					GraphesData_E_E[i][j][4] = GraphesData_E_Z[i][j][7]
							.substring(x + 7);
				}
			}

		}
		/*
		for (var test = 0;test<GraphesData_V_V[0].length;test++){
			alert(GraphesData_V_V[0][test][0]+'\n'+GraphesData_V_V[0][test][1]+'\n'+
					GraphesData_V_V[0][test][2]+'\n')
		}
		 */
	}
	//Partie 8: Requête de données pour générer un tableau bidimensionnel de deux images sélectionnées par l'utilisateur actuel (data_V1,data_V2,data_E1,data_E2)
	{
		var moleculeNo = [];
		for (i = 0; i < GraphesData_Name.length; i++) {
			if (graphNum[0] == GraphesData_Name[i])
				moleculeNo[0] = i;
			if (graphNum[1] == GraphesData_Name[i])
				moleculeNo[1] = i;
		}
		var data_V1 = [];
		var data_V2 = [];
		var data_E1_O = [];
		var data_E2_O = [];
		for (i = 0; i < GraphesData_V_V[moleculeNo[0]].length; i++) {
			data_V1[i] = [];
			data_V1[i][0] = GraphesData_V_V[moleculeNo[0]][i][0];
			data_V1[i][1] = GraphesData_V_V[moleculeNo[0]][i][1];
			data_V1[i][2] = GraphesData_V_V[moleculeNo[0]][i][2];
		}
		/*
		for (i=0;data_V1.length;i++)
			alert("nodename:"+data_V1[i][0]+"\n"+"x:"+data_V1[i][1]+"\n"+"y:"+data_V1[i][2]);
		 */
		for (i = 0; i < GraphesData_V_V[moleculeNo[1]].length; i++) {
			data_V2[i] = [];
			data_V2[i][0] = GraphesData_V_V[moleculeNo[1]][i][0];
			data_V2[i][1] = GraphesData_V_V[moleculeNo[1]][i][1];
			data_V2[i][2] = GraphesData_V_V[moleculeNo[1]][i][2];
		}
		for (i = 0; i < GraphesData_E_E[moleculeNo[0]].length; i++) {
			data_E1_O[i] = [];
			data_E1_O[i][0] = GraphesData_E_E[moleculeNo[0]][i][0];
			data_E1_O[i][1] = GraphesData_E_E[moleculeNo[0]][i][1];
			data_E1_O[i][2] = GraphesData_E_E[moleculeNo[0]][i][2];
			data_E1_O[i][3] = GraphesData_E_E[moleculeNo[0]][i][3];
			if (data_E1_O[i][2] > 1) {
				data_E1_O[i][4] = GraphesData_E_E[moleculeNo[0]][i][4];
			}
		}
		/*
		for (i=0;data_E1_O.length;i++)
			alert("from:"+data_E1_O[i][0]+"\n"+"to:"+data_E1_O[i][1]+"\n"+"frequency:"+data_E1_O[i][2]+"\n"+"angle0:"+data_E1_O[i][3]);
		 */
		for (i = 0; i < GraphesData_E_E[moleculeNo[1]].length; i++) {
			data_E2_O[i] = [];
			data_E2_O[i][0] = GraphesData_E_E[moleculeNo[1]][i][0];
			data_E2_O[i][1] = GraphesData_E_E[moleculeNo[1]][i][1];
			data_E2_O[i][2] = GraphesData_E_E[moleculeNo[1]][i][2];
			data_E2_O[i][3] = GraphesData_E_E[moleculeNo[1]][i][3];
			if (data_E2_O[i][2] > 1) {
				data_E2_O[i][4] = GraphesData_E_E[moleculeNo[1]][i][4];
			}
		}
	}
	//Partie 9: Le processus de déchargement des données traite data_E1_O et data_E2_O comme data_E1 et data_E2.
	{
		var data_E1 = [];
		var data_E2 = [];
		var data_E1_length = data_E1_O.length;
		var data_E2_length = data_E2_O.length;
		var freAll = 0;
		for (var o = 0; o < data_E1_length; o++) {
			data_E1[o] = [];
			data_E1_O[o - freAll][2] = Number(data_E1_O[o - freAll][2]);
			for (var fre = 1; fre <= data_E1_O[o - freAll][2]; fre++) {
				if (fre == data_E1_O[o - freAll][2]) {
					data_E1[o][0] = data_E1_O[o - freAll][0];
					data_E1[o][1] = data_E1_O[o - freAll][1];
					data_E1[o][2] = data_E1_O[o - freAll][2 + fre];
				} else {
					data_E1[o][0] = data_E1_O[o - freAll][1];
					data_E1[o][1] = data_E1_O[o - freAll][0];
					data_E1[o][2] = data_E1_O[o - freAll][2 + fre];
					o++;
					freAll++;
					data_E1_length++;
					data_E1[o] = [];
				}
			}
		}
		freAll = 0;
		for (o = 0; o < data_E2_length; o++) {
			data_E2[o] = [];
			data_E2_O[o - freAll][2] = Number(data_E2_O[o - freAll][2]);
			for (fre = 1; fre <= data_E2_O[o - freAll][2]; fre++) {
				data_E2[o][0] = data_E2_O[o - freAll][0];
				data_E2[o][1] = data_E2_O[o - freAll][1];
				data_E2[o][2] = data_E2_O[o - freAll][2 + fre];
				if (fre != data_E2_O[o - freAll][2]) {
					o++;
					freAll++;
					data_E2_length++;
					data_E2[o] = [];
				}
			}
		}
	}
	//Partie 10：Pré-traitement des données, améliorez la disponibilité des données grâce à la conversion de format, générez des données tridimensionnelles data_V et data_E
	{
		var data_V = [];
		var data_E = [];
		var graphnum = graphNum.length;
		for (var k = 0; k < graphnum; k++) {
			var data_V_add = '';
			var data_V_length;
			var data_E_length;
			data_V[k] = [];
			data_E[k] = [];
			var data_V_con = [];
			var data_E_con = [];
			switch (k) {
			case 0:
				data_V_length = data_V1.length;
				data_E_length = data_E1.length;
				data_V_add = 'a';
				for (var s = 0; s < data_V_length; s++) {
					data_V_con[s] = [];
					data_V_con[s][0] = data_V1[s][0];
					data_V_con[s][1] = data_V1[s][1];
					data_V_con[s][2] = data_V1[s][2];
				}
				for (s = 0; s < data_E_length; s++) {
					data_E_con[s] = [];
					data_E_con[s][0] = data_E1[s][0];
					data_E_con[s][1] = data_E1[s][1];
					data_E_con[s][2] = data_E1[s][2];
				}
				break;
			case 1:
				data_V_length = data_V2.length;
				data_E_length = data_E2.length;
				data_V_add = 'b';
				for (s = 0; s < data_V_length; s++) {
					data_V_con[s] = [];
					data_V_con[s][0] = data_V2[s][0];
					data_V_con[s][1] = data_V2[s][1];
					data_V_con[s][2] = data_V2[s][2];
				}
				for (s = 0; s < data_E_length; s++) {
					data_E_con[s] = [];
					data_E_con[s][0] = data_E2[s][0];
					data_E_con[s][1] = data_E2[s][1];
					data_E_con[s][2] = data_E2[s][2];
				}
				break;
			default:
				alert("Le nombre d'images par défaut est 2, veuillez ajouter un case.");
			}
			for (var v = 0; v < data_V_length; v++) {
				data_V[k][v] = [];
				data_V[k][v][0] = data_V_add + data_V_con[v][0].toString();
				data_V[k][v][1] = Number(data_V_con[v][1]);
				data_V[k][v][2] = Number(data_V_con[v][2]);
			}
			for (var e = 0; e < data_E_length; e++) {
				data_E[k][e] = [];
				data_E[k][e][0] = data_V_add + data_E_con[e][0].toString();
				data_E[k][e][1] = data_V_add + data_E_con[e][1].toString();
				data_E[k][e][2] = Number(data_E_con[e][2]);
			}
		}
		var data_VC = [];
		for (var c = 0; c < data_VC0.length - 1; c++) {
			data_VC[c] = [];
			data_VC[c][0] = data_VC0[c][0];
			data_VC[c][1] = data_VC0[c][1];
		}
		for (var c = 0; c < data_VC.length; c++) {
			data_VC[c][0] = 'a' + data_VC[c][0].toString();
			data_VC[c][1] = 'b' + data_VC[c][1].toString();
		}
	}
	//Partie 11
	/*
	Visualisation - tracez les deux images séparément et montrez la connexion des points des deux graphiques dans le graphique total.
	Optimisation du code - Utilise des tableaux tridimensionnels et des fonctions pour parcourir le code pertinent, éliminant environ 750 lignes de code non-pratique, améliorant ainsi l'applicabilité du code.
	Amélioration du système dynamique - force centripète de 0.2, simulation de double coeur, poids de marge variable, répulsion de point variable.
	 */
	{
		for (var justpre = 0; justpre < 3; justpre++) {
			var k;
			var flag;
			if (justpre == 0) {
				k = 0;
				flag = 0;
			} else if (justpre == 1) {
				k = 1;
				flag = 0;

			} else {
				k = 0;
				flag = 1;
			}
			//alert("k="+k+"\n"+"flag="+flag);
	        if(flag == 0){
	            switch(k)
	            {
	                case 0:
	                    var graph_cplex1 = echarts.init(document.getElementById('graph_cplex1'));
	                    var data_V_length = data_V1.length;
	                    var graphName = graphNum[0];
	                    break;
	                case 1:
	                    var graph_cplex2 = echarts.init(document.getElementById('graph_cplex2'));
	                    data_V_length = data_V2.length;
	                    graphName = graphNum[1];
	                    break;
	                default:
	                    alert("Le nombre d'images par défaut est 2, veuillez ajouter un case.");
	            }
	            var symbolpre = 1.8;
	            var xNodepre = 1;
	        }
	        else{
	            var graph_cplex = echarts.init(document.getElementById('graph_cplex'));
	            var graphpre = document.getElementById('graph_cplex');
	            xNodepre = graphpre.offsetWidth/3*2;
	            graphName = "La visualisation de GrapheMatching";
	            symbolpre = 1.5;
	        }
	        var data_c=[];
	        var link_c=[];
	        for(var graphNo = 0;graphNo <= flag;graphNo++) {
	            var kk = k + graphNo;
	            var xNode = kk*flag*xNodepre;
	            xNode = Number(xNode);
	            for (var i = 0; i < data_V[kk].length; i++) {//dessiner les nœuds
	                data_c.push
	                (
	                    {
	                        name: data_V[kk][i][0],
	                        showPosition: '('+data_V[kk][i][1]+','+data_V[kk][i][2]+')',
	                        value: 5 * symbolpre ,
	                        symbol: 'circle',
	                        symbolSize: 5 * symbolpre ,
	                        x: data_V[kk][i][1]+ xNode,
	                        y: data_V[kk][i][2] ,
	                        itemStyle: {
	                            normal: {
	                                color: '#000000'
	                            }
	                        },
	                        label: {
	                            emphasis: {
	                                show: false
	                            }
	                        }
	                    }
	                );
	            }
	            for (var j = 0; j < data_E[kk].length; j++) {//dessiner les arcs
	                var lineWidth = 3;
	                var lineValue = 1;
	                var curvenesspre = 1000;
	                link_c.push
	                (
	                    {
	                        source: data_E[kk][j][0],
	                        target: data_E[kk][j][1],
	                        value: lineValue,
	                        lineStyle: {
	                            normal: {
	                                color: '#000000',
	                                width: lineWidth,
	                                type: 'solid',
	                                curveness: data_E[kk][j][2]/curvenesspre,
	                                opacity: 0.75
	                            }
	                        },
	                        symbol: ['arrow', 'arrow'],
	                        symbolSize: [0, 0]
	                    }
	                );
	            }
	        }
	        if (flag == 1) {//dessiner des lignes entre deux graphes
	            for (c = 0; c < data_VC.length; c++) {
	                link_c.push
	                (
	                    {
	                        source: data_VC[c][0],
	                        target: data_VC[c][1],
	                        value: 1,
	                        lineStyle: {
	                            normal: {
	                                color: '#FF0000',
	                                width: 2,
	                                type: 'dashed',
	                                curveness: 0,
	                                opacity: 0.7
	                            }
	                        },
	                        symbol: ['arrow', 'arrow'],
	                        symbolSize: [0, 0]
	                    }
	                );
	            }
	        }
	        var option = {
	            title: {
	                text: graphName
	            },
	            tooltip: {},
	            backgroundColor : '#FFFFFF',
	            toolbox: {
	                show: true,
	                feature: {
	                    restore: {show: true},
	                    saveAsImage: {show: true}
	                }
	            },
	            series: [
	                {
	                    type: 'graph',
	                    tooltip: {
	                        formatter: function (node) {
	                            if (!node.data.name) {
	                                return null;
	                            } else {
	                                return node.data.name+ ':'+ node.data.showPosition;
	                            }
	                        }
	                    },
	                    layout: 'none',
	                    coordinateSystem: null,
	                    ribbonType: true,
	                    roam: true,
	                    nodeScaleRatio: 0.6,
	                    focusNodeAdjacency: true,
	                    cursor: 'pointer',
	                    edgeSymbol: ['circle', 'circle'],
	                    edgeSymbolSize: [0, 0],
	                    data: data_c,
	                    links: link_c
	                }
	            ]
	        };
	        if(flag == 0) {
	            switch (k) {
	                case 0:
	                    graph_cplex1.setOption(option);
	                    break;
	                case 1:
	                    graph_cplex2.setOption(option);
	                    break;
	                default:
	                    alert("Le nombre d'images par défaut est 2, veuillez ajouter un case.");
	            }
	        }
	        else {
	            graph_cplex.setOption(option);
	        }
	    }
	}
}

function draw(iflag) {
	if (iflag == 0) {
		alert("Veuillez mettre à jour le formulaire en premier");
	} else {
		var getDat = document.getElementById('getGraphesData').value;
		if (getDat.indexOf('molecule_') >= 0) {
			//alert("MUTA");
			document.getElementById("graph").style.display = "";
			getGraphesData();
		} else if (getDat.indexOf('sampletest') >= 0) {
			//alert("PAH");
			document.getElementById("graph").style.display = "";
			getSimpleData();
		} else {
			alert("Ne supporte pas la visualisation de tels graphiques")
		}
	}
}