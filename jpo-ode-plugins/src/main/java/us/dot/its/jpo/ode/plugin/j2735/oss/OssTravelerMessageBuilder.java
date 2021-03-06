package us.dot.its.jpo.ode.plugin.j2735.oss;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.time.ZonedDateTime;

import com.oss.asn1.Coder;
import com.oss.asn1.EncodeFailedException;
import com.oss.asn1.EncodeNotSupportedException;

import us.dot.its.jpo.ode.j2735.J2735;
import us.dot.its.jpo.ode.j2735.dsrc.Angle;
import us.dot.its.jpo.ode.j2735.dsrc.Circle;
import us.dot.its.jpo.ode.j2735.dsrc.ComputedLane;
import us.dot.its.jpo.ode.j2735.dsrc.ComputedLane.OffsetXaxis;
import us.dot.its.jpo.ode.j2735.dsrc.ComputedLane.OffsetYaxis;
import us.dot.its.jpo.ode.j2735.dsrc.DeltaAngle;
import us.dot.its.jpo.ode.j2735.dsrc.DescriptiveName;
import us.dot.its.jpo.ode.j2735.dsrc.DirectionOfUse;
import us.dot.its.jpo.ode.j2735.dsrc.DistanceUnits;
import us.dot.its.jpo.ode.j2735.dsrc.ExitService;
import us.dot.its.jpo.ode.j2735.dsrc.Extent;
import us.dot.its.jpo.ode.j2735.dsrc.GenericSignage;
import us.dot.its.jpo.ode.j2735.dsrc.GeographicalPath;
import us.dot.its.jpo.ode.j2735.dsrc.GeographicalPath.Description;
import us.dot.its.jpo.ode.j2735.dsrc.GeometricProjection;
import us.dot.its.jpo.ode.j2735.dsrc.HeadingSlice;
import us.dot.its.jpo.ode.j2735.dsrc.LaneDataAttribute;
import us.dot.its.jpo.ode.j2735.dsrc.LaneDataAttributeList;
import us.dot.its.jpo.ode.j2735.dsrc.LaneID;
import us.dot.its.jpo.ode.j2735.dsrc.LaneWidth;
import us.dot.its.jpo.ode.j2735.dsrc.Latitude;
import us.dot.its.jpo.ode.j2735.dsrc.Longitude;
import us.dot.its.jpo.ode.j2735.dsrc.MergeDivergeNodeAngle;
import us.dot.its.jpo.ode.j2735.dsrc.MinuteOfTheYear;
import us.dot.its.jpo.ode.j2735.dsrc.MsgCRC;
import us.dot.its.jpo.ode.j2735.dsrc.MsgCount;
import us.dot.its.jpo.ode.j2735.dsrc.NodeAttributeLL;
import us.dot.its.jpo.ode.j2735.dsrc.NodeAttributeLLList;
import us.dot.its.jpo.ode.j2735.dsrc.NodeAttributeSetLL;
import us.dot.its.jpo.ode.j2735.dsrc.NodeAttributeSetXY;
import us.dot.its.jpo.ode.j2735.dsrc.NodeAttributeXY;
import us.dot.its.jpo.ode.j2735.dsrc.NodeAttributeXYList;
import us.dot.its.jpo.ode.j2735.dsrc.NodeLL;
import us.dot.its.jpo.ode.j2735.dsrc.NodeListLL;
import us.dot.its.jpo.ode.j2735.dsrc.NodeListXY;
import us.dot.its.jpo.ode.j2735.dsrc.NodeOffsetPointLL;
import us.dot.its.jpo.ode.j2735.dsrc.NodeOffsetPointXY;
import us.dot.its.jpo.ode.j2735.dsrc.NodeSetLL;
import us.dot.its.jpo.ode.j2735.dsrc.NodeSetXY;
import us.dot.its.jpo.ode.j2735.dsrc.NodeXY;
import us.dot.its.jpo.ode.j2735.dsrc.Node_LL_24B;
import us.dot.its.jpo.ode.j2735.dsrc.Node_LL_28B;
import us.dot.its.jpo.ode.j2735.dsrc.Node_LL_32B;
import us.dot.its.jpo.ode.j2735.dsrc.Node_LL_36B;
import us.dot.its.jpo.ode.j2735.dsrc.Node_LL_44B;
import us.dot.its.jpo.ode.j2735.dsrc.Node_LL_48B;
import us.dot.its.jpo.ode.j2735.dsrc.Node_LLmD_64b;
import us.dot.its.jpo.ode.j2735.dsrc.Node_XY_20b;
import us.dot.its.jpo.ode.j2735.dsrc.Node_XY_22b;
import us.dot.its.jpo.ode.j2735.dsrc.Node_XY_24b;
import us.dot.its.jpo.ode.j2735.dsrc.Node_XY_26b;
import us.dot.its.jpo.ode.j2735.dsrc.Node_XY_28b;
import us.dot.its.jpo.ode.j2735.dsrc.Node_XY_32b;
import us.dot.its.jpo.ode.j2735.dsrc.OffsetLL_B12;
import us.dot.its.jpo.ode.j2735.dsrc.OffsetLL_B14;
import us.dot.its.jpo.ode.j2735.dsrc.OffsetLL_B16;
import us.dot.its.jpo.ode.j2735.dsrc.OffsetLL_B18;
import us.dot.its.jpo.ode.j2735.dsrc.OffsetLL_B22;
import us.dot.its.jpo.ode.j2735.dsrc.OffsetLL_B24;
import us.dot.its.jpo.ode.j2735.dsrc.OffsetSystem;
import us.dot.its.jpo.ode.j2735.dsrc.Offset_B10;
import us.dot.its.jpo.ode.j2735.dsrc.Offset_B11;
import us.dot.its.jpo.ode.j2735.dsrc.Offset_B12;
import us.dot.its.jpo.ode.j2735.dsrc.Offset_B13;
import us.dot.its.jpo.ode.j2735.dsrc.Offset_B14;
import us.dot.its.jpo.ode.j2735.dsrc.Offset_B16;
import us.dot.its.jpo.ode.j2735.dsrc.Radius_B12;
import us.dot.its.jpo.ode.j2735.dsrc.RegionList;
import us.dot.its.jpo.ode.j2735.dsrc.RegionOffsets;
import us.dot.its.jpo.ode.j2735.dsrc.RegionPointSet;
import us.dot.its.jpo.ode.j2735.dsrc.RegulatorySpeedLimit;
import us.dot.its.jpo.ode.j2735.dsrc.RoadRegulatorID;
import us.dot.its.jpo.ode.j2735.dsrc.RoadSegmentID;
import us.dot.its.jpo.ode.j2735.dsrc.RoadSegmentReferenceID;
import us.dot.its.jpo.ode.j2735.dsrc.RoadwayCrownAngle;
import us.dot.its.jpo.ode.j2735.dsrc.SSPindex;
import us.dot.its.jpo.ode.j2735.dsrc.Scale_B12;
import us.dot.its.jpo.ode.j2735.dsrc.SegmentAttributeLL;
import us.dot.its.jpo.ode.j2735.dsrc.SegmentAttributeLLList;
import us.dot.its.jpo.ode.j2735.dsrc.SegmentAttributeXY;
import us.dot.its.jpo.ode.j2735.dsrc.SegmentAttributeXYList;
import us.dot.its.jpo.ode.j2735.dsrc.ShapePointSet;
import us.dot.its.jpo.ode.j2735.dsrc.SpeedLimit;
import us.dot.its.jpo.ode.j2735.dsrc.SpeedLimitList;
import us.dot.its.jpo.ode.j2735.dsrc.SpeedLimitType;
import us.dot.its.jpo.ode.j2735.dsrc.TravelerDataFrame;
import us.dot.its.jpo.ode.j2735.dsrc.TravelerDataFrame.Content;
import us.dot.its.jpo.ode.j2735.dsrc.TravelerDataFrame.Regions;
import us.dot.its.jpo.ode.j2735.dsrc.TravelerDataFrameList;
import us.dot.its.jpo.ode.j2735.dsrc.TravelerInformation;
import us.dot.its.jpo.ode.j2735.dsrc.URL_Base;
import us.dot.its.jpo.ode.j2735.dsrc.URL_Short;
import us.dot.its.jpo.ode.j2735.dsrc.UniqueMSGID;
import us.dot.its.jpo.ode.j2735.dsrc.ValidRegion;
import us.dot.its.jpo.ode.j2735.dsrc.ValidRegion.Area;
import us.dot.its.jpo.ode.j2735.dsrc.Velocity;
import us.dot.its.jpo.ode.j2735.dsrc.WorkZone;
import us.dot.its.jpo.ode.j2735.dsrc.Zoom;
import us.dot.its.jpo.ode.j2735.itis.ITIScodesAndText;
import us.dot.its.jpo.ode.plugin.j2735.J2735TravelerInformationMessage;
import us.dot.its.jpo.ode.plugin.j2735.TimFieldValidator;
import us.dot.its.jpo.ode.util.CodecUtils;
import us.dot.its.jpo.ode.util.DateTimeUtils;

public class OssTravelerMessageBuilder {
   private TravelerInformation travelerInfo;

   public TravelerInformation buildTravelerInformation(
         J2735TravelerInformationMessage tim)
         throws ParseException, EncodeFailedException, EncodeNotSupportedException, IllegalArgumentException {

      travelerInfo = new TravelerInformation();
      TimFieldValidator.validateMessageCount(tim.getMsgCnt());
      travelerInfo.setMsgCnt(new MsgCount(tim.getMsgCnt()));
      travelerInfo.setTimeStamp(
            new MinuteOfTheYear(getMinuteOfTheYear(tim.getTimeStamp())));
      ByteBuffer buf = ByteBuffer.allocate(9).put((byte) 0).putLong(tim.getPacketID());
      travelerInfo.setPacketID(new UniqueMSGID(buf.array()));
      TimFieldValidator.validateURL(tim.getUrlB());
      travelerInfo.setUrlB(new URL_Base(tim.getUrlB()));
      travelerInfo.setDataFrames(buildDataFrames(tim));

      return travelerInfo;
   }

   private TravelerDataFrameList buildDataFrames(
         J2735TravelerInformationMessage tim) 
         throws ParseException {
      TravelerDataFrameList dataFrames = new TravelerDataFrameList();

      TimFieldValidator.validateFrameCount(tim.getDataframes().length);
      int len = tim.getDataframes().length;
      for (int i = 0; i < len; i++) {
         J2735TravelerInformationMessage.DataFrame inputDataFrame = tim.getDataframes()[i];
         TravelerDataFrame dataFrame = new TravelerDataFrame();

         // Part I, header
         OssTIMHeaderBuilder.buildTimHeader(inputDataFrame, dataFrame);

         // -- Part II, Applicable Regions of Use
         TimFieldValidator.validateHeaderIndex(inputDataFrame.getsspLocationRights());
         dataFrame.setSspLocationRights(new SSPindex(inputDataFrame.getsspLocationRights()));
         dataFrame.setRegions(buildRegions(inputDataFrame.getRegions()));

         // -- Part III, Content
         TimFieldValidator.validateHeaderIndex(inputDataFrame.getsspMsgTypes());
         dataFrame.setSspMsgRights1(new SSPindex(inputDataFrame.getsspMsgTypes())); // allowed
         // message
         // types
         TimFieldValidator.validateHeaderIndex(inputDataFrame.getsspMsgContent());
         dataFrame.setSspMsgRights2(new SSPindex(inputDataFrame.getsspMsgContent())); // allowed
         // message
         // content
         dataFrame.setContent(buildContent(inputDataFrame));
         TimFieldValidator.validateURLShort(inputDataFrame.getUrl());
         dataFrame.setUrl(new URL_Short(inputDataFrame.getUrl()));
         dataFrames.add(dataFrame);
      }
      return dataFrames;
   }

   public String encodeTravelerInformationToHex() 
         throws EncodeFailedException, EncodeNotSupportedException {
      Coder coder = J2735.getPERUnalignedCoder();
      ByteArrayOutputStream sink = new ByteArrayOutputStream();
      coder.encode(travelerInfo, sink);
      byte[] bytes = sink.toByteArray();
      return CodecUtils.toHex(bytes);
   }

   public Content buildContent(J2735TravelerInformationMessage.DataFrame inputDataFrame) {
      String contentType = inputDataFrame.getContent();
      String[] codes = inputDataFrame.getItems();
      Content content = new Content();
      if ("Advisory".equals(contentType)) {
         content.setAdvisory(buildAdvisory(codes));
      } else if ("Work Zone".equals(contentType)) {
         content.setWorkZone(buildWorkZone(codes));
      } else if ("Speed Limit".equals(contentType)) {
         content.setSpeedLimit(buildSpeedLimit(codes));
      } else if ("Exit Service".equals(contentType)) {
         content.setExitService(buildExitService(codes));
      } else if ("Generic Signage".equals(contentType)) {
         content.setGenericSign(buildGenericSignage(codes));
      }
      return content;
   }

   public ITIScodesAndText buildAdvisory(String[] codes) {
      ITIScodesAndText itisText = new ITIScodesAndText();
      for (String code : codes) {
         TimFieldValidator.validateITISCodes(code);
         ITIScodesAndText.Sequence_ seq = new ITIScodesAndText.Sequence_();
         ITIScodesAndText.Sequence_.Item item = new ITIScodesAndText.Sequence_.Item();
         item.setItis(Long.parseLong(code));
         seq.setItem(item);
         itisText.add(seq);
      }
      return itisText;
   }

   public WorkZone buildWorkZone(String[] codes) {
      WorkZone wz = new WorkZone();
      for (String code : codes) {
         TimFieldValidator.validateContentCodes(code);
         WorkZone.Sequence_ seq = new WorkZone.Sequence_();
         WorkZone.Sequence_.Item item = new WorkZone.Sequence_.Item();
         item.setItis(Long.parseLong(code));
         seq.setItem(item);
         wz.add(seq);
      }
      return wz;
   }

   public SpeedLimit buildSpeedLimit(String[] codes) {
      SpeedLimit sl = new SpeedLimit();
      for (String code : codes) {
         TimFieldValidator.validateContentCodes(code);
         SpeedLimit.Sequence_ seq = new SpeedLimit.Sequence_();
         SpeedLimit.Sequence_.Item item = new SpeedLimit.Sequence_.Item();
         item.setItis(Long.parseLong(code));
         seq.setItem(item);
         sl.add(seq);
      }
      return sl;
   }

   public ExitService buildExitService(String[] codes) {
      ExitService es = new ExitService();
      for (String code : codes) {
         TimFieldValidator.validateContentCodes(code);
         ExitService.Sequence_ seq = new ExitService.Sequence_();
         ExitService.Sequence_.Item item = new ExitService.Sequence_.Item();
         item.setItis(Long.parseLong(code));
         seq.setItem(item);
         es.add(seq);
      }
      return es;
   }

   public GenericSignage buildGenericSignage(String[] codes) {
      GenericSignage gs = new GenericSignage();
      for (String code : codes) {
         TimFieldValidator.validateContentCodes(code);
         GenericSignage.Sequence_ seq = new GenericSignage.Sequence_();
         GenericSignage.Sequence_.Item item = new GenericSignage.Sequence_.Item();
         item.setItis(Long.parseLong(code));
         seq.setItem(item);
         gs.add(seq);
      }
      return gs;
   }

   private Regions buildRegions(J2735TravelerInformationMessage.DataFrame.Region[] inputRegions) {
      Regions regions = new Regions();
      for (J2735TravelerInformationMessage.DataFrame.Region inputRegion : inputRegions) {
         GeographicalPath geoPath = new GeographicalPath();
         Description description = new Description();
         TimFieldValidator.validateGeoName(inputRegion.getName());
         geoPath.setName(new DescriptiveName(inputRegion.getName()));
         TimFieldValidator.validateRoadID(inputRegion.getRegulatorID());
         TimFieldValidator.validateRoadID(inputRegion.getSegmentID());
         geoPath.setId(new RoadSegmentReferenceID(new RoadRegulatorID(inputRegion.getRegulatorID()),
               new RoadSegmentID(inputRegion.getSegmentID())));
         geoPath.setAnchor(OssPosition3D.position3D(inputRegion.getAnchorPosition()));
         TimFieldValidator.validateLaneWidth(inputRegion.getLaneWidth());
         geoPath.setLaneWidth(new LaneWidth(inputRegion.getLaneWidth()));
         TimFieldValidator.validateDirectionality(inputRegion.getDirectionality());
         geoPath.setDirectionality(new DirectionOfUse(inputRegion.getDirectionality()));
         geoPath.setClosedPath(inputRegion.isClosedPath());
         TimFieldValidator.validateHeading(inputRegion.getDirection());
         geoPath.setDirection(getHeadingSlice(inputRegion.getDirection()));

         if ("path".equals(inputRegion.getDescription())) {
            OffsetSystem offsetSystem = new OffsetSystem();
            TimFieldValidator.validateZoom(inputRegion.getPath().getScale());
            offsetSystem.setScale(new Zoom(inputRegion.getPath().getScale()));
            if ("xy".equals(inputRegion.getPath().getType())) {
               if (inputRegion.getPath().getNodes() != null) {
                  offsetSystem.setOffset(new OffsetSystem.Offset());
                  offsetSystem.offset.setXy(buildNodeXYList(inputRegion.getPath().getNodes()));
               } else {
                  offsetSystem.setOffset(new OffsetSystem.Offset());
                  offsetSystem.offset.setXy(buildComputedLane(inputRegion.getPath().getComputedLane()));
               }
            } else if ("ll".equals(inputRegion.getPath().getType()) && inputRegion.getPath().getNodes().length > 0) {
               offsetSystem.setOffset(new OffsetSystem.Offset());
               offsetSystem.offset.setLl(buildNodeLLList(inputRegion.getPath().getNodes()));
            }
            description.setPath(offsetSystem);
            geoPath.setDescription(description);
         } else if ("geometry".equals(inputRegion.getDescription())) {
            GeometricProjection geo = new GeometricProjection();
            TimFieldValidator.validateHeading(inputRegion.getGeometry().getDirection());
            geo.setDirection(getHeadingSlice(inputRegion.getGeometry().getDirection()));
            TimFieldValidator.validateExtent(inputRegion.getGeometry().getExtent());
            geo.setExtent(new Extent(inputRegion.getGeometry().getExtent()));
            TimFieldValidator.validateLaneWidth(inputRegion.getGeometry().getLaneWidth());
            geo.setLaneWidth(new LaneWidth(inputRegion.getGeometry().getLaneWidth()));
            geo.setCircle(buildGeoCircle(inputRegion.getGeometry()));
            description.setGeometry(geo);
            geoPath.setDescription(description);

         } else { // oldRegion
            ValidRegion validRegion = new ValidRegion();
            TimFieldValidator.validateHeading(inputRegion.getOldRegion().getDirection());
            validRegion.setDirection(getHeadingSlice(inputRegion.getOldRegion().getDirection()));
            TimFieldValidator.validateExtent(inputRegion.getOldRegion().getExtent());
            validRegion.setExtent(new Extent(inputRegion.getOldRegion().getExtent()));
            Area area = new Area();
            if ("shapePointSet".equals(inputRegion.getOldRegion().getArea())) {
               ShapePointSet sps = new ShapePointSet();
               sps.setAnchor(OssPosition3D.position3D(inputRegion.getOldRegion().getShapepoint().getPosition()));
               TimFieldValidator.validateLaneWidth(inputRegion.getOldRegion().getShapepoint().getLaneWidth());
               sps.setLaneWidth(new LaneWidth(inputRegion.getOldRegion().getShapepoint().getLaneWidth()));
               TimFieldValidator.validateDirectionality(inputRegion.getOldRegion().getShapepoint().getDirectionality());
               sps.setDirectionality(
                     new DirectionOfUse(inputRegion.getOldRegion().getShapepoint().getDirectionality()));
               if (inputRegion.getOldRegion().getShapepoint().getNodexy() != null) {
                  sps.setNodeList(buildNodeXYList(inputRegion.getOldRegion().getShapepoint().getNodexy()));
               } else {
                  sps.setNodeList(buildComputedLane(inputRegion.getOldRegion().getShapepoint().getComputedLane()));
               }
               area.setShapePointSet(sps);
               validRegion.setArea(area);
            } else if ("regionPointSet".equals(inputRegion.getOldRegion().getArea())) {
               RegionPointSet rps = new RegionPointSet();
               rps.setAnchor(OssPosition3D.position3D(inputRegion.getOldRegion().getRegionPoint().getPosition()));
               TimFieldValidator.validateZoom(inputRegion.getOldRegion().getRegionPoint().getScale());
               rps.setScale(new Zoom(inputRegion.getOldRegion().getRegionPoint().getScale()));
               RegionList rl = buildRegionOffsets(inputRegion.getOldRegion().getRegionPoint().getRegionList());
               rps.setNodeList(rl);
               area.setRegionPointSet(rps);
               validRegion.setArea(area);
            } else {// circle
               area.setCircle(buildOldCircle(inputRegion.getOldRegion()));
               validRegion.setArea(area);
            }
            description.setOldRegion(validRegion);
            geoPath.setDescription(description);
         }
         regions.add(geoPath);
      }
      return regions;
   }

   public RegionList buildRegionOffsets(
         J2735TravelerInformationMessage.DataFrame.Region.OldRegion.RegionPoint.RegionList[] list) {
      RegionList myList = new RegionList();
      for (int i = 0; i < list.length; i++) {
         RegionOffsets ele = new RegionOffsets();
         TimFieldValidator.validatex16Offset(list[i].getxOffset());
         ele.setXOffset(new OffsetLL_B16(list[i].getxOffset()));
         TimFieldValidator.validatey16Offset(list[i].getyOffset());
         ele.setYOffset(new OffsetLL_B16(list[i].getyOffset()));
         TimFieldValidator.validatez16Offset(list[i].getzOffset());
         ele.setZOffset(new OffsetLL_B16(list[i].getzOffset()));
         myList.add(ele);
      }
      return myList;
   }

   public Circle buildGeoCircle(J2735TravelerInformationMessage.DataFrame.Region.Geometry geo) {
      Circle circle = new Circle();
      circle.setCenter(OssPosition3D.position3D(geo.getCircle().getPosition()));
      TimFieldValidator.validateRadius(geo.getCircle().getRadius());
      circle.setRadius(new Radius_B12(geo.getCircle().getRadius()));
      TimFieldValidator.validateUnits(geo.getCircle().getUnits());
      circle.setUnits(new DistanceUnits(geo.getCircle().getUnits()));
      return circle;
   }

   public Circle buildOldCircle(J2735TravelerInformationMessage.DataFrame.Region.OldRegion reg) {
      Circle circle = new Circle();
      circle.setCenter(OssPosition3D.position3D(reg.getCircle().getPosition()));
      TimFieldValidator.validateRadius(reg.getCircle().getRadius());
      circle.setRadius(new Radius_B12(reg.getCircle().getRadius()));
      TimFieldValidator.validateUnits(reg.getCircle().getUnits());
      circle.setUnits(new DistanceUnits(reg.getCircle().getUnits()));
      return circle;
   }

   public NodeListXY buildNodeXYList(J2735TravelerInformationMessage.NodeXY[] inputNodes) {
      NodeListXY nodeList = new NodeListXY();
      NodeSetXY nodes = new NodeSetXY();
      for (int i = 0; i < inputNodes.length; i++) {
         J2735TravelerInformationMessage.NodeXY point = inputNodes[i];

         NodeXY node = new NodeXY();
         NodeOffsetPointXY nodePoint = new NodeOffsetPointXY();

         switch (point.getDelta()) {
         case "node-XY1":
            TimFieldValidator.validateB10Offset(point.getX());
            TimFieldValidator.validateB10Offset(point.getY());
            Node_XY_20b xy = new Node_XY_20b(new Offset_B10(point.getX()), new Offset_B10(point.getY()));
            nodePoint.setNode_XY1(xy);
            break;
         case "node-XY2":
            TimFieldValidator.validateB11Offset(point.getX());
            TimFieldValidator.validateB11Offset(point.getY());
            Node_XY_22b xy2 = new Node_XY_22b(new Offset_B11(point.getX()), new Offset_B11(point.getY()));
            nodePoint.setNode_XY2(xy2);
            break;
         case "node-XY3":
            TimFieldValidator.validateB12Offset(point.getX());
            TimFieldValidator.validateB12Offset(point.getY());
            Node_XY_24b xy3 = new Node_XY_24b(new Offset_B12(point.getX()), new Offset_B12(point.getY()));
            nodePoint.setNode_XY3(xy3);
            break;
         case "node-XY4":
            TimFieldValidator.validateB13Offset(point.getX());
            TimFieldValidator.validateB13Offset(point.getY());
            Node_XY_26b xy4 = new Node_XY_26b(new Offset_B13(point.getX()), new Offset_B13(point.getY()));
            nodePoint.setNode_XY4(xy4);
            break;
         case "node-XY5":
            TimFieldValidator.validateB14Offset(point.getX());
            TimFieldValidator.validateB14Offset(point.getY());
            Node_XY_28b xy5 = new Node_XY_28b(new Offset_B14(point.getX()), new Offset_B14(point.getY()));
            nodePoint.setNode_XY5(xy5);
            break;
         case "node-XY6":
            TimFieldValidator.validateB16Offset(point.getX());
            TimFieldValidator.validateB16Offset(point.getY());
            Node_XY_32b xy6 = new Node_XY_32b(new Offset_B16(point.getX()), new Offset_B16(point.getY()));
            nodePoint.setNode_XY6(xy6);
            break;
         case "node-LatLon":
            TimFieldValidator.validateLatitude(point.getNodeLat());
            TimFieldValidator.validateLongitude(point.getNodeLong());
            Node_LLmD_64b nodeLatLong = new Node_LLmD_64b(new Longitude(point.getNodeLong()),
                  new Latitude(point.getNodeLat()));
            nodePoint.setNode_LatLon(nodeLatLong);
            break;
         default:
            break;
         }

         node.setDelta(nodePoint);
         if (point.getAttributes() != null) {
            NodeAttributeSetXY attributes = new NodeAttributeSetXY();

            if (point.getAttributes().getLocalNodes().length > 0) {
               NodeAttributeXYList localNodeList = new NodeAttributeXYList();
               for (J2735TravelerInformationMessage.LocalNode localNode : point.getAttributes().getLocalNodes()) {
                  localNodeList.add(new NodeAttributeXY(localNode.getType()));
               }
               attributes.setLocalNode(localNodeList);
            }

            if (point.getAttributes().getDisabledLists().length > 0) {
               SegmentAttributeXYList disabledNodeList = new SegmentAttributeXYList();
               for (J2735TravelerInformationMessage.DisabledList disabledList : point.getAttributes().getDisabledLists()) {
                  disabledNodeList.add(new SegmentAttributeXY(disabledList.getType()));
               }
               attributes.setDisabled(disabledNodeList);
            }

            if (point.getAttributes().getEnabledLists().length > 0) {
               SegmentAttributeXYList enabledNodeList = new SegmentAttributeXYList();
               for (J2735TravelerInformationMessage.EnabledList enabledList : point.getAttributes().getEnabledLists()) {
                  enabledNodeList.add(new SegmentAttributeXY(enabledList.getType()));
               }
               attributes.setEnabled(enabledNodeList);
            }

            if (point.getAttributes().getDataLists().length > 0) {
               LaneDataAttributeList dataNodeList = new LaneDataAttributeList();
               for (J2735TravelerInformationMessage.DataList dataList : point.getAttributes().getDataLists()) {

                  LaneDataAttribute dataAttribute = new LaneDataAttribute();

                  dataAttribute.setPathEndPointAngle(new DeltaAngle(dataList.getPathEndpointAngle()));
                  dataAttribute.setLaneCrownPointCenter(new RoadwayCrownAngle(dataList.getLaneCrownCenter()));
                  dataAttribute.setLaneCrownPointLeft(new RoadwayCrownAngle(dataList.getLaneCrownLeft()));
                  dataAttribute.setLaneCrownPointRight(new RoadwayCrownAngle(dataList.getLaneCrownRight()));
                  dataAttribute.setLaneAngle(new MergeDivergeNodeAngle(dataList.getLaneAngle()));

                  SpeedLimitList speedDataList = new SpeedLimitList();
                  for (J2735TravelerInformationMessage.SpeedLimits speedLimit : dataList.getSpeedLimits()) {
                     speedDataList.add(new RegulatorySpeedLimit(new SpeedLimitType(speedLimit.getType()),
                           new Velocity(speedLimit.getVelocity())));
                  }

                  dataAttribute.setSpeedLimits(speedDataList);
                  dataNodeList.add(dataAttribute);
               }

               attributes.setData(dataNodeList);
            }

            attributes.setDWidth(new Offset_B10(point.getAttributes().getdWidth()));
            attributes.setDElevation(new Offset_B10(point.getAttributes().getdElevation()));

            node.setAttributes(attributes);

         }
         nodes.add(node);
      }

      nodeList.setNodes(nodes);
      return nodeList;
   }

   private NodeListXY buildComputedLane(J2735TravelerInformationMessage.ComputedLane inputLane) {
      NodeListXY nodeList = new NodeListXY();

      ComputedLane computedLane = new ComputedLane();
      OffsetXaxis ox = new OffsetXaxis();
      OffsetYaxis oy = new OffsetYaxis();

      computedLane.setReferenceLaneId(new LaneID(inputLane.getLaneID()));
      if (inputLane.getOffsetLargeX() > 0) {
         ox.setLarge(inputLane.getOffsetLargeX());
         computedLane.offsetXaxis = ox;
      } else {
         ox.setSmall(inputLane.getOffsetSmallX());
         computedLane.offsetXaxis = ox;
      }

      if (inputLane.getOffsetLargeY() > 0) {
         oy.setLarge(inputLane.getOffsetLargeY());
         computedLane.offsetYaxis = oy;
      } else {
         oy.setSmall(inputLane.getOffsetSmallY());
         computedLane.offsetYaxis = oy;
      }
      computedLane.setRotateXY(new Angle(inputLane.getAngle()));
      computedLane.setScaleXaxis(new Scale_B12(inputLane.getxScale()));
      computedLane.setScaleYaxis(new Scale_B12(inputLane.getyScale()));

      nodeList.setComputed(computedLane);
      return nodeList;
   }

   public NodeListLL buildNodeLLList(J2735TravelerInformationMessage.NodeXY[] inputNodes) {
      NodeListLL nodeList = new NodeListLL();
      NodeSetLL nodes = new NodeSetLL();
      for (int i = 0; i < inputNodes.length; i++) {
         J2735TravelerInformationMessage.NodeXY point = inputNodes[i];

         NodeLL node = new NodeLL();
         NodeOffsetPointLL nodePoint = new NodeOffsetPointLL();

         switch (point.getDelta()) {
         case "node-LL1":
            TimFieldValidator.validateLL12Offset(point.getNodeLat());
            TimFieldValidator.validateLL12Offset(point.getNodeLong());
            Node_LL_24B xy1 = new Node_LL_24B(new OffsetLL_B12(point.getNodeLat()),
                  new OffsetLL_B12(point.getNodeLong()));
            nodePoint.setNode_LL1(xy1);
            break;
         case "node-LL2":
            TimFieldValidator.validateLL14Offset(point.getNodeLat());
            TimFieldValidator.validateLL14Offset(point.getNodeLong());
            Node_LL_28B xy2 = new Node_LL_28B(new OffsetLL_B14(point.getNodeLat()),
                  new OffsetLL_B14(point.getNodeLong()));
            nodePoint.setNode_LL2(xy2);
            break;
         case "node-LL3":
            TimFieldValidator.validateLL16Offset(point.getNodeLat());
            TimFieldValidator.validateLL16Offset(point.getNodeLong());
            Node_LL_32B xy3 = new Node_LL_32B(new OffsetLL_B16(point.getNodeLat()),
                  new OffsetLL_B16(point.getNodeLong()));
            nodePoint.setNode_LL3(xy3);
            break;
         case "node-LL4":
            TimFieldValidator.validateLL18Offset(point.getNodeLat());
            TimFieldValidator.validateLL18Offset(point.getNodeLong());
            Node_LL_36B xy4 = new Node_LL_36B(new OffsetLL_B18(point.getNodeLat()),
                  new OffsetLL_B18(point.getNodeLong()));
            nodePoint.setNode_LL4(xy4);
            break;
         case "node-LL5":
            TimFieldValidator.validateLL22Offset(point.getNodeLat());
            TimFieldValidator.validateLL22Offset(point.getNodeLong());
            Node_LL_44B xy5 = new Node_LL_44B(new OffsetLL_B22(point.getNodeLat()),
                  new OffsetLL_B22(point.getNodeLong()));
            nodePoint.setNode_LL5(xy5);
            break;
         case "node-LL6":
            TimFieldValidator.validateLL24Offset(point.getNodeLat());
            TimFieldValidator.validateLL24Offset(point.getNodeLong());
            Node_LL_48B xy6 = new Node_LL_48B(new OffsetLL_B24(point.getNodeLat()),
                  new OffsetLL_B24(point.getNodeLong()));
            nodePoint.setNode_LL6(xy6);
            break;
         case "node-LatLon":
            TimFieldValidator.validateLatitude(point.getNodeLat());
            TimFieldValidator.validateLongitude(point.getNodeLong());
            Node_LLmD_64b nodeLatLong = new Node_LLmD_64b(new Longitude(point.getNodeLong()),
                  new Latitude(point.getNodeLat()));
            nodePoint.setNode_LatLon(nodeLatLong);
            break;
         default:
            break;
         }

         node.setDelta(nodePoint);
         if (point.getAttributes() != null) {
            NodeAttributeSetLL attributes = new NodeAttributeSetLL();

            if (point.getAttributes().getLocalNodes().length > 0) {
               NodeAttributeLLList localNodeList = new NodeAttributeLLList();
               for (J2735TravelerInformationMessage.LocalNode localNode : point.getAttributes().getLocalNodes()) {
                  localNodeList.add(new NodeAttributeLL(localNode.getType()));
               }
               attributes.setLocalNode(localNodeList);
            }

            if (point.getAttributes().getDisabledLists().length > 0) {
               SegmentAttributeLLList disabledNodeList = new SegmentAttributeLLList();
               for (J2735TravelerInformationMessage.DisabledList disabledList : point.getAttributes().getDisabledLists()) {
                  disabledNodeList.add(new SegmentAttributeLL(disabledList.getType()));
               }
               attributes.setDisabled(disabledNodeList);
            }

            if (point.getAttributes().getEnabledLists().length > 0) {
               SegmentAttributeLLList enabledNodeList = new SegmentAttributeLLList();
               for (J2735TravelerInformationMessage.EnabledList enabledList : point.getAttributes().getEnabledLists()) {
                  enabledNodeList.add(new SegmentAttributeLL(enabledList.getType()));
               }
               attributes.setEnabled(enabledNodeList);
            }

            if (point.getAttributes().getDataLists().length > 0) {
               LaneDataAttributeList dataNodeList = new LaneDataAttributeList();
               for (J2735TravelerInformationMessage.DataList dataList : point.getAttributes().getDataLists()) {

                  LaneDataAttribute dataAttribute = new LaneDataAttribute();

                  dataAttribute.setPathEndPointAngle(new DeltaAngle(dataList.getPathEndpointAngle()));
                  dataAttribute.setLaneCrownPointCenter(new RoadwayCrownAngle(dataList.getLaneCrownCenter()));
                  dataAttribute.setLaneCrownPointLeft(new RoadwayCrownAngle(dataList.getLaneCrownLeft()));
                  dataAttribute.setLaneCrownPointRight(new RoadwayCrownAngle(dataList.getLaneCrownRight()));
                  dataAttribute.setLaneAngle(new MergeDivergeNodeAngle(dataList.getLaneAngle()));

                  SpeedLimitList speedDataList = new SpeedLimitList();
                  for (J2735TravelerInformationMessage.SpeedLimits speedLimit : dataList.getSpeedLimits()) {
                     speedDataList.add(new RegulatorySpeedLimit(new SpeedLimitType(speedLimit.getType()),
                           new Velocity(speedLimit.getVelocity())));
                  }

                  dataAttribute.setSpeedLimits(speedDataList);
                  dataNodeList.add(dataAttribute);
               }

               attributes.setData(dataNodeList);
            }

            attributes.setDWidth(new Offset_B10(point.getAttributes().getdWidth()));
            attributes.setDElevation(new Offset_B10(point.getAttributes().getdElevation()));

            node.setAttributes(attributes);
         }
         nodes.add(node);
      }
      nodeList.setNodes(nodes);
      return nodeList;
   }
   
   public static long getMinuteOfTheYear(String timestamp) throws ParseException {
      ZonedDateTime start = DateTimeUtils.isoDateTime(timestamp);
      long diff = DateTimeUtils.difference(DateTimeUtils.isoDateTime(start.getYear() + "-01-01T00:00:00+00:00"), start);
      long minutes = diff / 60000;
      TimFieldValidator.validateStartTime(minutes);
      return minutes;
   }
   
   public static HeadingSlice getHeadingSlice(String heading) {
      if (heading == null || heading.length() == 0) {
         return new HeadingSlice(new byte[] { 0x00, 0x00 });
      } else {
         short result = 0;
         for (int i = 0; i < 16; i++) {
            if (heading.charAt(i) == '1') {
               result |= 1;
            }
            result <<= 1;
         }
         return new HeadingSlice(ByteBuffer.allocate(2).putShort(result).array());
      }
   }
   
   public static MsgCRC getMsgCrc(String sum) {
      if (sum == null || sum.length() == 0) {
         return new MsgCRC(new byte[] { 0X00, 0X00 });
      } else {
         short result = 0;
         for (int i = 0; i < 16; i++) {
            if (sum.charAt(i) == '1') {
               result |= 1;
            }
            result <<= 1;
         }
         return new MsgCRC(ByteBuffer.allocate(2).putShort(result).array());
      }
   }

}
