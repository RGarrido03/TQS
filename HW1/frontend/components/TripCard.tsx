import { Trip } from "@/types/trip";
import {
  Card,
  CardHeader,
  CardBody,
  CardFooter,
  Divider,
  Skeleton,
} from "@nextui-org/react";
import { useCookies } from "next-client-cookies";
import { useRouter } from "next/navigation";
import { MaterialSymbol } from "react-material-symbols";

type TripCardProps = {
  trip: Trip;
  isLoaded: boolean;
  clickable?: boolean;
};

export default function TripCard({
  trip,
  isLoaded,
  clickable = true,
}: TripCardProps) {
  const cookies = useCookies();
  const router = useRouter();

  return isLoaded ? (
    <Card
      key={trip.id}
      isPressable={clickable && trip.freeSeats > 0}
      isDisabled={trip.freeSeats === 0}
      onPress={
        clickable
          ? () => {
              cookies.set("trip", trip.id.toString());
              router.push("/reservation");
            }
          : undefined
      }
    >
      <CardHeader className="flex flex-col justify-start items-start">
        <p className="text-lg font-bold">
          {trip.departureTime.toLocaleString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
            hour: "numeric",
            minute: "numeric",
          })}
        </p>
        <p className="text-small text-default-500">
          Arrival at{" "}
          {trip.arrivalTime.toLocaleString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
            hour: "numeric",
            minute: "numeric",
          })}
        </p>
      </CardHeader>
      <Divider />
      <CardBody>
        <div className="grid grid-cols-2">
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="flight_takeoff" size={20} />
            <p>{trip.departure.name}</p>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="payments" size={20} />
            <p>
              {trip.price.toLocaleString("pt-PT", {
                style: "currency",
                currency: cookies.get("currency") || "EUR",
              })}
            </p>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="flight_land" size={20} />
            <p>{trip.arrival.name}</p>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="directions_bus" size={20} />
            <p>{trip.bus.capacity}-seat bus</p>
          </div>
        </div>
      </CardBody>
      <Divider />
      <CardFooter>
        {trip.freeSeats === 0 ? "No" : trip.freeSeats} seats available
      </CardFooter>
    </Card>
  ) : (
    <Card>
      <CardHeader className="flex flex-col justify-start items-start">
        <Skeleton className="rounded">
          <p className="text-lg font-bold">Departure time</p>
        </Skeleton>
        <Skeleton className="rounded">
          <p className="text-small text-default-500">Arrival time</p>
        </Skeleton>
      </CardHeader>
      <Divider />
      <CardBody>
        <div className="grid grid-cols-2">
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="flight_takeoff" size={20} />
            <Skeleton className="rounded">
              <p>Departure</p>
            </Skeleton>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="payments" size={20} />
            <Skeleton className="rounded">
              <p>Price</p>
            </Skeleton>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="flight_land" size={20} />
            <Skeleton className="rounded">
              <p>Arrival</p>
            </Skeleton>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="directions_bus" size={20} />
            <Skeleton className="rounded">
              <p>Capacity</p>
            </Skeleton>
          </div>
        </div>
      </CardBody>
      <Divider />
      <CardFooter>
        <Skeleton className="rounded">
          <p>0 seats available</p>
        </Skeleton>
      </CardFooter>
    </Card>
  );
}
